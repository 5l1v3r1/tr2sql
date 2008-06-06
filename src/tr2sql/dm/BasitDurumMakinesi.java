package tr2sql.dm;

import tr2sql.cozumleyici.*;
import tr2sql.db.SorguTasiyici;
import tr2sql.db.Kolon;
import tr2sql.db.KolonKisitlamaBileseni;

import java.util.List;
import java.util.ArrayList;

public class BasitDurumMakinesi {

    private Durum suAnkiDurum = Durum.BASLA;
    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<SorguCumleBileseni> bilesenler;

    private List<KolonKisitlamaBileseni> kolonBilesenleri =
            new ArrayList<KolonKisitlamaBileseni>();

    private List<Kolon> kolonlar = new ArrayList<Kolon>();
    private List<String> bilgiler = new ArrayList<String>();


    public BasitDurumMakinesi(List<SorguCumleBileseni> bilesenler) {
        this.bilesenler = bilesenler;
    }

    public SorguTasiyici islet() {
        for (int i = 0; i < bilesenler.size(); i++) {
            SorguCumleBileseni sorguCumleBileseni = bilesenler.get(i);
            switch (sorguCumleBileseni.tip()) {
                case KOLON:
            }
        }
        return sorguTasiyici;
    }

    

    private Durum gecis(Gecis gecis, SorguCumleBileseni bilesen) {
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