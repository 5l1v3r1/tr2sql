package tr2sql.cozumleyici;

import tr2sql.db.*;

import java.util.ArrayList;
import java.util.List;

public class BasitDurumMakinesi {

    public enum Durum {
        //basit durum makinesinde bu durumlarımız var.
        BASLA,
        KOLON_ALINDI,
        COKLU_KOLON_ALINDI,
        BILGI_ALINDI,
        COKLU_BILGI_ALINDI,
        KIYAS_ALINDI,
        SONUC_KOLONU_ALINDI,
        OLMAK_ALINDI,
        TABLO_BULUNDU,
        SONUC_KISITLAMA_SAYISI_BEKLE,
        SONUC_KISITLAMA_SAYISI_ALINDI,
        ISLEM_BELIRLENDI,
    }

    private Durum suAnkiDurum = Durum.BASLA;

    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<CumleBileseni> bilesenler;

    private List<KolonBileseni> kolonBilesenleri = new ArrayList<KolonBileseni>();
    private List<BilgiBileseni> bilgiBilesenleri = new ArrayList<BilgiBileseni>();
    private List<Kolon> sonucKolonlari = new ArrayList<Kolon>();

    // calisma sirasianda olup bitenlerin bir yerde tutulmasini saglar.
    private StringBuilder cozumRaporu = new StringBuilder();

    //içine ayrıştırdığımız cümle bileşenleri bu kısma gelir. 
    public BasitDurumMakinesi(List<CumleBileseni> bilesenler) {
        this.bilesenler = bilesenler;
    }
    
    //durum makinesini işleten metot burasıdır. (state machine)
    public SorguTasiyici islet() {
        for (CumleBileseni bilesen : bilesenler) {
            if (bilesen.tip == CumleBilesenTipi.TANIMSIZ) {
                //gereksiz ve işlenemeyen bileşenler burda ihmal ettirilir.
                raporla("Uyari: Islenemeyen bilesen:" + bilesen.icerik);
                continue;
            }
            suAnkiDurum = gecis(bilesen);
        }
        // sorgu tasiyiciya toplanan bazi bilgileri ekle.
        sorguTasiyici.sonucKolonlari = sonucKolonlari;
        sorguTasiyici.raporla(cozumRaporu.toString());

        return sorguTasiyici;
    }

    private Durum gecis(CumleBileseni bilesen) {

        CumleBilesenTipi gecis = bilesen.tip();

        switch (suAnkiDurum) {

            case BASLA:
                switch (gecis) {
                    case KOLON:
                        // numarasi...
                        return kolonBileseniGecisi(bilesen);
                    case TABLO:
                        // iscileri..
                        return tabloBileseniGecisi(bilesen);
                        // ilk...
                    case SONUC_MIKTAR:
                        return Durum.SONUC_KISITLAMA_SAYISI_BEKLE;
                }
                break;

            case KOLON_ALINDI:
                switch (gecis) {
                    case KISITLAMA_BILGISI:
                        // numarasi "5" ...
                        return bilgiBileseniGecisi(bilesen);
                    case KOLON:
                        // adi ve soyadi ...
                        return cokluKolonBileseniGecisi(bilesen);
                }
                break;

            case COKLU_KOLON_ALINDI:
                switch (gecis) {
                    case KISITLAMA_BILGISI:
                        // adi ve soyadi "a" ...
                        return bilgiBileseniGecisi(bilesen);
                    case KOLON:
                        // adi, soyadi ve okulu ...
                        return cokluKolonBileseniGecisi(bilesen);
                }
                break;

            case COKLU_BILGI_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        return olmakBileseniGecisi(bilesen);
                    case KIYASLAYICI:
                        // adi "a" veya "b" ile baslayan
                        KiyaslamaBileseni kb = (KiyaslamaBileseni) bilesen;
                        if (kb.olumsuzuk)
                            kb.kiyasTipi = kb.kiyasTipi.tersi();
                        for (BilgiBileseni bilgiBileseni : bilgiBilesenleri) {
                            bilgiBileseni.setKiyasTipi(kb.kiyasTipi);
                        }
                        return Durum.KIYAS_ALINDI;

                    case KOLON:
                        kisitlamaIsle();
                        return kolonBileseniGecisi(bilesen);

                    case KISITLAMA_BILGISI:
                        return cokluBilgiBileseniGecisi(bilesen);
                }
                break;

            case BILGI_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        return olmakBileseniGecisi(bilesen);
                    case KIYASLAYICI:
                        KiyaslamaBileseni kb = (KiyaslamaBileseni) bilesen;
                        if (kb.olumsuzuk)
                            kb.kiyasTipi = kb.kiyasTipi.tersi();
                        for (BilgiBileseni bilgiBileseni : bilgiBilesenleri) {
                            bilgiBileseni.setKiyasTipi(kb.kiyasTipi);
                        }
                        return Durum.KIYAS_ALINDI;
                    case KOLON:
                        kisitlamaIsle();
                        return kolonBileseniGecisi(bilesen);

                    case KISITLAMA_BILGISI:
                        return cokluBilgiBileseniGecisi(bilesen);
                }
                break;

            case OLMAK_ALINDI:
                switch (gecis) {
                    case KOLON:
                        // numarasi 5 olan ve soyadi....
                        return kolonBileseniGecisi(bilesen);
                    case TABLO:
                        return tabloBileseniGecisi(bilesen);
                    case SONUC_MIKTAR:
                        return Durum.SONUC_KISITLAMA_SAYISI_BEKLE;
                    case ISLEM:
                        return islemBileseniGecisi(bilesen);
                }
                break;

            case KIYAS_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        return olmakBileseniGecisi(bilesen);
                    case TABLO:
                        kisitlamaIsle();
                        return tabloBileseniGecisi(bilesen);
                    case KOLON:
                        kisitlamaIsle();
                        return kolonBileseniGecisi(bilesen);
                }
                break;

            case TABLO_BULUNDU:
                switch (gecis) {
                    case ISLEM:
                        return islemBileseniGecisi(bilesen);
                    case KOLON:
                        return sonucKolonuGecisi(bilesen);
                    case SONUC_MIKTAR:
                        return Durum.SONUC_KISITLAMA_SAYISI_BEKLE;
                }
                break;

            case SONUC_KISITLAMA_SAYISI_BEKLE:
                switch (gecis) {
                    case SAYI:
                        SayiBileseni sb = (SayiBileseni) bilesen;
                        sorguTasiyici.sonucMiktarKisitlamaDegeri = sb.deger;
                        return Durum.SONUC_KISITLAMA_SAYISI_ALINDI;
                }
                break;

            case SONUC_KISITLAMA_SAYISI_ALINDI:
                switch (gecis) {
                    case TABLO:
                        return tabloBileseniGecisi(bilesen);
                    case ISLEM:
                        return islemBileseniGecisi(bilesen);
                    case KOLON:
                        return sonucKolonuGecisi(bilesen);
                }
                break;


            case SONUC_KOLONU_ALINDI:
                switch (gecis) {
                    case ISLEM:
                        return islemBileseniGecisi(bilesen);
                    case KOLON:
                        return sonucKolonuGecisi(bilesen);
                }
                break;

        }
        throw new SQLUretimHatasi("Beklenmeyen cumle bileseni:" + bilesen.toString() + ". " +
                "Su anki durum:" + suAnkiDurum.name());
    }

    /**
     * toplanan kisitlamaya dair kolon, bilgi ve bilgi kiyas bilgileri bu metod ile
     * sorgu tasiyiya KolonKisitlamaZincirBileseni olarak eklenir.
     */
    private void kisitlamaIsle() {
        for (KolonBileseni kolonBileseni : kolonBilesenleri) {
            KolonKisitlamaBileseni kb = new KolonKisitlamaBileseni(
                    kolonBileseni.getKolon(), bilgiBilesenleri);
            KolonKisitlamaZincirBileseni kkzb = new KolonKisitlamaZincirBileseni(kb, kolonBileseni.getOnBaglac());
            sorguTasiyici.kolonKisitlamaZinciri.add(kkzb);
        }
        kolonKiyasTemizle();
    }

    private Durum olmakBileseniGecisi(CumleBileseni bilesen) {
        OlmakBIleseni ob = (OlmakBIleseni) bilesen;
        if (ob.olumsuz())
            for (BilgiBileseni bilgiBileseni : bilgiBilesenleri) {
                bilgiBileseni.kiyasTipi = bilgiBileseni.kiyasTipi.tersi();
            }
        kisitlamaIsle();
        return Durum.OLMAK_ALINDI;
    }

    private Durum cokluBilgiBileseniGecisi(CumleBileseni bilesen) {
        BilgiBileseni kb = (BilgiBileseni) bilesen;
        if (!kb.baglacVar())
            kb.setOnBaglac(BaglacTipi.VEYA);
        bilgiBilesenleri.add(kb);
        return Durum.COKLU_BILGI_ALINDI;
    }

    private Durum cokluKolonBileseniGecisi(CumleBileseni bilesen) {
        KolonBileseni kb = (KolonBileseni) bilesen;
        if (!kb.baglacVar())
            kb.setOnBaglac(BaglacTipi.VE);
        kolonBilesenleri.add(kb);
        return Durum.COKLU_KOLON_ALINDI;
    }

    private Durum sonucKolonuGecisi(CumleBileseni bilesen) {
        Kolon k = ((KolonBileseni) bilesen).getKolon();
        sonucKolonlari.add(k);
        return Durum.SONUC_KOLONU_ALINDI;
    }

    private Durum islemBileseniGecisi(CumleBileseni bilesen) {
        IslemBileseni b = (IslemBileseni) bilesen;
        if (b.olumsuz())
            raporla("Uyari: Islemi belirten eylem: " + bilesen.icerik() + ", olumsuzluk bilgisi iceriyor. Bu gozardi edilecek.");
        sorguTasiyici.islemTipi = b.getIslem();
        return Durum.ISLEM_BELIRLENDI;
    }

    private Durum tabloBileseniGecisi(CumleBileseni bilesen) {
        TabloBileseni tabloBil = (TabloBileseni) bilesen; //gelen bileşen tablo bilşeni olarak tanımlanır.
        sorguTasiyici.tablo = tabloBil.getTablo();
        return Durum.TABLO_BULUNDU;
    }

    private Durum kolonBileseniGecisi(CumleBileseni bilesen) {
        KolonBileseni kb = (KolonBileseni) bilesen;
        kolonBilesenleri.add(kb);
        if (kb.baglacVar())
            return Durum.COKLU_KOLON_ALINDI;
        else return Durum.KOLON_ALINDI;
    }

    private Durum bilgiBileseniGecisi(CumleBileseni bilesen) {
        BilgiBileseni kb = (BilgiBileseni) bilesen;
        bilgiBilesenleri.add(kb);
        if (kb.baglacVar())
            return Durum.COKLU_BILGI_ALINDI;
        else return Durum.BILGI_ALINDI;
    }

    private void raporla(String s) {
        cozumRaporu.append(s).append("\n");
    }

    private void kolonKiyasTemizle() {
        kolonBilesenleri = new ArrayList<KolonBileseni>();
        bilgiBilesenleri = new ArrayList<BilgiBileseni>();
    }

}