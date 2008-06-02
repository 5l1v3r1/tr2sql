package tr2sql.dm;

import tr2sql.cozumleyici.SorguCumleBileseni;
import tr2sql.cozumleyici.KolonBileseni;
import tr2sql.db.SorguTasiyici;
import tr2sql.db.Kolon;

import java.util.List;

//TODO Henuz islemiyor!
public class BasitDurumMakinesi {

    private Durum suAnkiDurum = Durum.BASLA;
    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<SorguCumleBileseni> bilesenler;

    public BasitDurumMakinesi(List<SorguCumleBileseni> bilesenler) {
        this.bilesenler = bilesenler;
        for (int i = 0; i < bilesenler.size(); i++) {
            SorguCumleBileseni sorguCumleBileseni = bilesenler.get(i);
            switch(sorguCumleBileseni.tip()) {
                case KOLON:
            }

        }
    }

    public Durum gecis(Gecis gecis, SorguCumleBileseni bilesen ) {
        switch (suAnkiDurum) {
            case BASLA:
                switch (gecis) {
                    case KOLON_I:
                        Kolon kolon = ((KolonBileseni) bilesen).getKolon();
                        sorguTasiyici.kolonKisitlamaBasla(kolon);
                        return Durum.KOLON_ALINDI;
                }
                break;

            case KOLON_ALINDI:
                 switch (gecis) {
                     case BILGI:


                 }
                break;

            case BILGI_ALINDI:
                switch (gecis) {
                    case KOLON_I:



                }
                break;
        }
        throw new IllegalStateException("Umulmayan durum!");
    }


}
