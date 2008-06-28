package tr2sql.cozumleyici;

import tr2sql.db.*;

import java.util.ArrayList;
import java.util.List;

public class BasitDurumMakinesi {

    public enum Durum {
        //basit durum makinesinde bu durumlarımız var.
        BASLA,
        SUTUN_ALINDI,
        COKLU_SUTUN_ALINDI,
        BILGI_ALINDI,
        SUTUN_ONCESI_BILGI_ALINDI,
        COKLU_BILGI_ALINDI,
        KIYAS_ALINDI,
        SONUC_SUTUNU_ALINDI,
        OLMAK_ALINDI,
        TABLO_BULUNDU,
        BASTA_TABLO_BULUNDU,
        SONUC_KISITLAMA_SAYISI_BEKLE,
        SONUC_KISITLAMA_SAYISI_ALINDI,
        SUTUN_NULL_KIYAS_ALINDI,
        SINIR_BELIRLENDI,
        ISLEM_BELIRLENDI,
        SAYMA_ALINDI,
        SON
    }

    private Durum suAnkiDurum = Durum.BASLA;

    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<CumleBileseni> bilesenler;

    private List<SutunBileseni> sutunBilesenleri = new ArrayList<SutunBileseni>();
    private List<BilgiBileseni> bilgiBilesenleri = new ArrayList<BilgiBileseni>();
    private List<Sutun> sonucSutunlari = new ArrayList<Sutun>();

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
        sorguTasiyici.sonucSutunlari = sonucSutunlari;
        sorguTasiyici.raporla(cozumRaporu.toString());
        sorguTasiyici.saymaSorgusu = saymaSorgusu;

        return sorguTasiyici;
    }

    private Durum gecis(CumleBileseni bilesen) {

        CumleBilesenTipi gecis = bilesen.tip();

        switch (suAnkiDurum) {

            case BASLA:
                switch (gecis) {
                    case SUTUN:
                        // numarasi...
                        return sutunBileseniGecisi(bilesen);
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
                        return Durum.SUTUN_ONCESI_BILGI_ALINDI;
                }
                break;

            case SUTUN_ALINDI:
                switch (gecis) {
                    case KISITLAMA_BILGISI:
                        // numarasi "5" ...
                        return bilgiBileseniGecisi(bilesen);
                    case SUTUN:
                        // adi ve soyadi ...
                        return cokluSutunBileseniGecisi(bilesen);
                    case KIYASLAYICI:
                        // adi olan ....
                        // soyadi bos olan ...
                        //sutunlar icin sadece bos, bos degil denetimi yapiyoruz.
                        return sutunSorasiKiyasGecisi(bilesen);
                    case OLMAK:
                        // adi, soyadi olan ...
                        return sutunSonrasiOlmakGecisi(bilesen);
                }
                break;

            case COKLU_SUTUN_ALINDI:
                switch (gecis) {
                    case KISITLAMA_BILGISI:
                        // adi ve soyadi "a" ...
                        return bilgiBileseniGecisi(bilesen);
                    case SUTUN:
                        // adi, soyadi ve okulu ...
                        return cokluSutunBileseniGecisi(bilesen);
                    case KIYASLAYICI:
                        // adi olan ....
                        // soyadi bos olan ...
                        return sutunSorasiKiyasGecisi(bilesen);
                    case OLMAK:
                        // adi, soyadi olan ...
                        return sutunSonrasiOlmakGecisi(bilesen);
                }
                break;

            case SAYMA_ALINDI:
                // calisanlardan numarasi ...
                switch (gecis) {
                    case SUTUN:
                        return sutunBileseniGecisi(bilesen);
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

                    case SUTUN:
                        kisitlamaIsle();
                        return sutunBileseniGecisi(bilesen);

                    case KISITLAMA_BILGISI:
                        return cokluBilgiBileseniGecisi(bilesen);
                }
                break;

            case SUTUN_ONCESI_BILGI_ALINDI:
                switch (gecis) {
                    case SUTUN:
                        SutunBileseni k = (SutunBileseni) bilesen;
                        sutunBilesenleri.add(k);
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
                    case SUTUN:
                        kisitlamaIsle();
                        return sutunBileseniGecisi(bilesen);

                    case KISITLAMA_BILGISI:
                        return cokluBilgiBileseniGecisi(bilesen);
                }
                break;

            case OLMAK_ALINDI:
                switch (gecis) {
                    case SUTUN:
                        // numarasi 5 olan ve soyadi....
                        return sutunBileseniGecisi(bilesen);
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
                    case SUTUN:
                        kisitlamaIsle();
                        return sutunBileseniGecisi(bilesen);
                    case SAY:
                        kisitlamaIsle();
                        saymaSorgusu = true;
                        return Durum.SAYMA_ALINDI;
                }
                break;

            case BASTA_TABLO_BULUNDU:
                // calisanlardan numarasi ...
                switch (gecis) {
                    case SUTUN:
                        return sutunBileseniGecisi(bilesen);
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
                    case SUTUN:
                        return sonucSutunuGecisi(bilesen);
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
                    case SUTUN:
                        return sonucSutunuGecisi(bilesen);
                }
                break;


            case SONUC_SUTUNU_ALINDI:
                switch (gecis) {
                    case ISLEM:
                        return islemBileseniGecisi(bilesen);
                    case SUTUN:
                        return sonucSutunuGecisi(bilesen);
                }
                break;

        }
        throw new SQLUretimHatasi("Beklenmeyen cumle bileseni:" + bilesen.toString() + ". " +
                "Su anki durum:" + suAnkiDurum.name());
    }

    /**
     * toplanan kisitlamaya dair sutun, bilgi ve bilgi kiyas bilgileri bu metod ile
     * sorgu tasiyiya SutunKisitlamaZincirBileseni olarak eklenir.
     */
    private void kisitlamaIsle() {
        for (SutunBileseni sutunBileseni : sutunBilesenleri) {
            SutunKisitlamaBileseni kb = new SutunKisitlamaBileseni(
                    sutunBileseni.getSutun(), bilgiBilesenleri, sutunBileseni.getOnBaglac());
            sorguTasiyici.sutunKisitlamalari.add(kb);
        }
        sutunKiyasTemizle();
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

    private Durum cokluSutunBileseniGecisi(CumleBileseni bilesen) {
        SutunBileseni kb = (SutunBileseni) bilesen;
        if (!kb.baglacVar())
            kb.setOnBaglac(BaglacTipi.VE);
        sutunBilesenleri.add(kb);
        return Durum.COKLU_SUTUN_ALINDI;
    }

    private Durum sonucSutunuGecisi(CumleBileseni bilesen) {
        Sutun k = ((SutunBileseni) bilesen).getSutun();
        sonucSutunlari.add(k);
        return Durum.SONUC_SUTUNU_ALINDI;
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

    private Durum sutunBileseniGecisi(CumleBileseni bilesen) {
        SutunBileseni kb = (SutunBileseni) bilesen;
        sutunBilesenleri.add(kb);
        if (kb.baglacVar())
            return Durum.COKLU_SUTUN_ALINDI;
        else return Durum.SUTUN_ALINDI;
    }

    private Durum bilgiBileseniGecisi(CumleBileseni bilesen) {
        BilgiBileseni kb = (BilgiBileseni) bilesen;
        bilgiBilesenleri.add(kb);
        if (kb.baglacVar())
            return Durum.COKLU_BILGI_ALINDI;
        else return Durum.BILGI_ALINDI;
    }

    private Durum sutunSorasiKiyasGecisi(CumleBileseni bilesen) {
        KiyaslamaBileseni kb = (KiyaslamaBileseni) bilesen;
        if (kb.kiyasTipi != KiyasTipi.NULL)
            throw new SQLUretimHatasi("Sutun bileseninden sonra sadece bos-null kiyaslama bileseni gelebilir. " +
                    "Gelen bilesen:" + kb.kiyasTipi.name());
        BilgiBileseni b = new BilgiBileseni("");
        b.setKiyasTipi(KiyasTipi.NULL);
        bilgiBilesenleri.add(b);
        return Durum.KIYAS_ALINDI;
    }

    private Durum sutunSonrasiOlmakGecisi(CumleBileseni bilesen) {
        OlmakBIleseni ob = (OlmakBIleseni) bilesen;
        KiyasTipi tip = KiyasTipi.NULL_DEGIL;
        if (ob.olumsuz())
            tip = tip.tersi();
        for (SutunBileseni kb : sutunBilesenleri) {
            BilgiBileseni bb = new BilgiBileseni("");
            bb.setKiyasTipi(tip);
            SutunKisitlamaBileseni kkb = new SutunKisitlamaBileseni(kb.getSutun(), bb, kb.getOnBaglac());
        }
        sutunKiyasTemizle();
        return Durum.OLMAK_ALINDI;
    }


    private void raporla(String s) {
        cozumRaporu.append(s).append("\n");
    }

    private void sutunKiyasTemizle() {
        sutunBilesenleri = new ArrayList<SutunBileseni>();
        bilgiBilesenleri = new ArrayList<BilgiBileseni>();
    }

}