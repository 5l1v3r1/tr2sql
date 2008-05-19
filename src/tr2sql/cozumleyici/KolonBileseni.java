package tr2sql.cozumleyici;

import tr2sql.db.Kolon;
import net.zemberek.yapi.Kelime;

/**
 * dilek
 */
public class KolonBileseni extends TemelCumleBileseni {

    Kolon kolon;
    String icerik;
    Kelime kelime;


    public KolonBileseni(Kolon kolon, Kelime kelime) {
        this.kolon = kolon;
        this.tip = CumleBilesenTipi.KOLON;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;
    }

    public Kolon getKolon() {
        return kolon;
    }
}
