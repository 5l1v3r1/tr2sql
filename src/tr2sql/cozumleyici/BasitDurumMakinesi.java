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
        KOLON_ONCESI_BILGI_ALINDI,
        COKLU_BILGI_ALINDI,
        KIYAS_ALINDI,
        SONUC_KOLONU_ALINDI,
        OLMAK_ALINDI,
        TABLO_BULUNDU,
        BASTA_TABLO_BULUNDU,
        SONUC_KISITLAMA_SAYISI_BEKLE,
        SONUC_KISITLAMA_SAYISI_ALINDI,
        KOLON_NULL_KIYAS_ALINDI,
        SINIR_BELIRLENDI,
        ISLEM_BELIRLENDI,
        SAYMA_ALINDI,
        SON
    }

    private Durum suAnkiDurum = Durum.BASLA;

    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<CumleBileseni> bilesenler;

    private List<KolonBileseni> kolonBilesenleri = new ArrayList<KolonBileseni>();
    private List<BilgiBileseni> bilgiBilesenleri = new ArrayList<BilgiBileseni>();
    private List<Kolon> sonucKolonlari = new ArrayList<Kolon>();

    // calisma sirasianda olup bitenlerin bir yerde tutulmasini saglar.
    private StringBuilder cozumRaporu = new StringBuilder();

    private boolean saymaSorgusu;

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
        // eger islenecek kisitlama kalmissa (bu bazi durumlarda oluyor.
        // mesela: kaç çalışanın ismi "A" ile başlıyor)
        kisitlamaIsle();
        // sorgu tasiyiciya toplanan bazi bilgileri ekle.
        sorguTasiyici.sonucKolonlari = sonucKolonlari;
        sorguTasiyici.raporla(cozumRaporu.toString());
        sorguTasiyici.saymaSorgusu = saymaSorgusu;

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
                    case SAY:
                        saymaSorgusu = true;
                        return Durum.SAYMA_ALINDI;
                        // "ali" adli
                    case KISITLAMA_BILGISI:
                        BilgiBileseni kb = (BilgiBileseni) bilesen;
                        bilgiBilesenleri.add(kb);
                        return Durum.KOLON_ONCESI_BILGI_ALINDI;
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
                    case KIYASLAYICI:
                        // adi olan ....
                        // soyadi bos olan ...
                        //kolonlar icin sadece bos, bos degil denetimi yapiyoruz.
                        return kolonSorasiKiyasGecisi(bilesen);
                    case OLMAK:
                        // adi, soyadi olan ...
                        return kolonSonrasiOlmakGecisi(bilesen);
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
                    case KIYASLAYICI:
                        // adi olan ....
                        // soyadi bos olan ...
                        return kolonSorasiKiyasGecisi(bilesen);
                    case OLMAK:
                        // adi, soyadi olan ...
                        return kolonSonrasiOlmakGecisi(bilesen);
                }
                break;

            case SAYMA_ALINDI:
                // calisanlardan numarasi ...
                switch (gecis) {
                    case KOLON:
                        return kolonBileseniGecisi(bilesen);
                    case TABLO:
                        // bu aslinda tam dogru degil..
                        TabloBileseni tabloBil = (TabloBileseni) bilesen;
                        sorguTasiyici.tablo = tabloBil.getTablo();
                        return Durum.BASTA_TABLO_BULUNDU;
                }
                break;

            case COKLU_BILGI_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        return olmakBileseniGecisi(bilesen);
                    case KIYASLAYICI:
                        // adi "a" veya "b" ile baslayan
                        KiyasBileseniGecisi(bilesen);
                        return Durum.KIYAS_ALINDI;

                    case KOLON:
                        kisitlamaIsle();
                        return kolonBileseniGecisi(bilesen);

                    case KISITLAMA_BILGISI:
                        return cokluBilgiBileseniGecisi(bilesen);
                }
                break;

            case KOLON_ONCESI_BILGI_ALINDI:
                switch (gecis) {
                    case KOLON:
                        KolonBileseni k = (KolonBileseni) bilesen;
                        kolonBilesenleri.add(k);
                        kisitlamaIsle();
                        return Durum.BASLA;
                    case KIYASLAYICI:
                        KiyaslamaBileseni kb = (KiyaslamaBileseni) bilesen;
                        if (kb.olumsuzuk)
                            kb.kiyasTipi = kb.kiyasTipi.tersi();
                        for (BilgiBileseni bilgiBileseni : bilgiBilesenleri) {
                            bilgiBileseni.setKiyasTipi(kb.kiyasTipi);
                        }
                        return Durum.KIYAS_ALINDI;
                }
                break;

            case BILGI_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        return olmakBileseniGecisi(bilesen);
                    case KIYASLAYICI:
                        KiyasBileseniGecisi(bilesen);
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
                    case SAY:
                        kisitlamaIsle();
                        saymaSorgusu = true;
                        return Durum.SAYMA_ALINDI;
                }
                break;

            case BASTA_TABLO_BULUNDU:
                // calisanlardan numarasi ...
                switch (gecis) {
                    case KOLON:
                        return kolonBileseniGecisi(bilesen);
                    case SAY:
                        saymaSorgusu = true;
                        return Durum.SAYMA_ALINDI;
                    case ISLEM:
                        return islemBileseniGecisi(bilesen);
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
                    kolonBileseni.getKolon(), bilgiBilesenleri, kolonBileseni.getOnBaglac());
            sorguTasiyici.kolonKisitlamalari.add(kb);
        }
        kolonKiyasTemizle();
    }

    private void KiyasBileseniGecisi(CumleBileseni bilesen) {
        KiyaslamaBileseni kb = (KiyaslamaBileseni) bilesen;
        if (kb.olumsuzuk)
            kb.kiyasTipi = kb.kiyasTipi.tersi();
        for (BilgiBileseni bilgiBileseni : bilgiBilesenleri) {
            bilgiBileseni.setKiyasTipi(kb.kiyasTipi);
        }
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
        return Durum.BASTA_TABLO_BULUNDU;
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

    private Durum kolonSorasiKiyasGecisi(CumleBileseni bilesen) {
        KiyaslamaBileseni kb = (KiyaslamaBileseni) bilesen;
        if (kb.kiyasTipi != KiyasTipi.NULL)
            throw new SQLUretimHatasi("Kolon bileseninden sonra sadece bos-null kiyaslama bileseni gelebilir. " +
                    "Gelen bilesen:" + kb.kiyasTipi.name());
        BilgiBileseni b = new BilgiBileseni("");
        b.setKiyasTipi(KiyasTipi.NULL);
        bilgiBilesenleri.add(b);
        return Durum.KIYAS_ALINDI;
    }

    private Durum kolonSonrasiOlmakGecisi(CumleBileseni bilesen) {
        OlmakBIleseni ob = (OlmakBIleseni) bilesen;
        KiyasTipi tip = KiyasTipi.NULL_DEGIL;
        if (ob.olumsuz())
            tip = tip.tersi();
        for (KolonBileseni kb : kolonBilesenleri) {
            BilgiBileseni bb = new BilgiBileseni("");
            bb.setKiyasTipi(tip);
            KolonKisitlamaBileseni kkb = new KolonKisitlamaBileseni(kb.getKolon(), bb, kb.getOnBaglac());
        }
        kolonKiyasTemizle();
        return Durum.OLMAK_ALINDI;
    }


    private void raporla(String s) {
        cozumRaporu.append(s).append("\n");
    }

    private void kolonKiyasTemizle() {
        kolonBilesenleri = new ArrayList<KolonBileseni>();
        bilgiBilesenleri = new ArrayList<BilgiBileseni>();
    }

}