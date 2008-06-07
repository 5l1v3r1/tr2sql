package tr2sql.dm;

import tr2sql.cozumleyici.*;
import tr2sql.db.Kolon;
import tr2sql.db.KolonKisitlamaBileseni;
import tr2sql.db.SorguTasiyici;
import tr2sql.db.BaglacTipi;

import java.util.ArrayList;
import java.util.List;

public class BasitDurumMakinesi {

    private Durum suAnkiDurum = Durum.BASLA;
    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<CumleBileseni> bilesenler;

    private List<KolonKisitlamaBileseni> kolonBilesenleri =
            new ArrayList<KolonKisitlamaBileseni>();

    private List<Kolon> kolonlar = new ArrayList<Kolon>();
    private List<String> bilgiler = new ArrayList<String>();


    public BasitDurumMakinesi(List<CumleBileseni> bilesenler) {
        this.bilesenler = bilesenler;
    }

    public SorguTasiyici islet() {
        for (int i = 0; i < bilesenler.size(); i++) {

            CumleBileseni bilesen = bilesenler.get(i);
            suAnkiDurum = gecis(gecisDegeriBul(bilesen), bilesen);
        }
        return sorguTasiyici;
    }

    private Gecis gecisDegeriBul(CumleBileseni cbi) {
        switch (cbi.tip()) {
            case ISLEM:
                return Gecis.ISLEM;
            case KISITLAMA_BILGISI:
                BilgiBileseni bb = (BilgiBileseni) cbi;
                if (bb.getOnBaglac() != BaglacTipi.YOK)
                    return Gecis.BAGLAC_BILGI;
                else return Gecis.BILGI;
            case KIYASLAYICI:
                return Gecis.KIYASLAMA;
            case KOLON:
                KolonBileseni kb = (KolonBileseni) cbi;
                if (kb.getOnBaglac() != BaglacTipi.YOK)
                    return Gecis.KOLON;
                else return Gecis.BAGLAC_KOLON;
            case TABLO:
                return Gecis.TABLO;
            case OLMAK:
                return Gecis.OLMAK;
            case SAYI:
                return Gecis.SAYI;
            case SONUC_MIKTAR:
        }
        return null;
    }

    private Durum gecis(Gecis gecis, CumleBileseni bilesen) {
        switch (suAnkiDurum) {

            case BASLA:
                switch (gecis) {
                    case KOLON:
                        Kolon kolon = ((KolonBileseni) bilesen).getKolon();
                        kolonlar.add(kolon);
                        return Durum.KOLON_ALINDI;
                    case TABLO:
                        TabloBileseni tabloBil = (TabloBileseni) bilesen;
                        sorguTasiyici.tablo = tabloBil.getTablo();
                        return Durum.TABLO_BULUNDU;
                }
                break;

            case KOLON_ALINDI:
                switch (gecis) {
                    case BILGI:
                        BilgiBileseni bil = ((BilgiBileseni) bilesen);
                        bilgiler.add(bil.icerik());
                        return Durum.BILGI_ALINDI;
                }
                break;

            case BILGI_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        break;
                    case KIYASLAMA:
                        break;
                    case BAGLAC_KOLON:
                        break;
                    case BAGLAC_BILGI:
                        break;
                }
                break;

            case OLMAK_ALINDI:
                switch (gecis) {
                    case KOLON:
                        break;
                }
                break;

            case KIYAS_ALINDI:
                switch (gecis) {

                }
                break;

            case TABLO_BULUNDU:
                switch (gecis) {
                    case ISLEM:
                        IslemBileseni b = (IslemBileseni) bilesen;
                        sorguTasiyici.islemTipi = b.getIslem();
                        return Durum.ISLEM_BELIRLENDI;
                }
                break;
        }
        throw new IllegalStateException("Umulmayan durum!");
    }

    private void kolonKisitlamaIsle() {
        for (Kolon kolon : kolonlar) {
            for (String s : bilgiler) {

            }
        }
    }

    private class BilgiKiyasIkilisi {
        String bilgi;

    }
}