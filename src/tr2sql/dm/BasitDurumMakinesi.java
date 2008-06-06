package tr2sql.dm;

import tr2sql.cozumleyici.SorguCumleBileseni;
import tr2sql.cozumleyici.KolonBileseni;
import tr2sql.cozumleyici.BilgiBileseni;
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
        for (int i = 0; i < bilesenler.size(); i++) {
            SorguCumleBileseni sorguCumleBileseni = bilesenler.get(i);
            switch (sorguCumleBileseni.tip()) {
                case KOLON:
            }

        }
    }

    public Durum gecis(Gecis gecis, SorguCumleBileseni bilesen) {
        switch (suAnkiDurum) {
            case BASLA:
                switch (gecis) {
                    case KOLON:
                        Kolon kolon = ((KolonBileseni) bilesen).getKolon();
                        kolonlar.add(kolon);
                        return Durum.KOLON_ALINDI;
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
                    case KIYASLAMA: break;
                    case BAGLAC_KOLON: break;
                    case BAGLAC_BILGI: break;


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