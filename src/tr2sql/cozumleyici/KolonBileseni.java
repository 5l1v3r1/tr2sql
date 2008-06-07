package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.Kolon;

/**
 * dilek
 */
public class KolonBileseni extends TemelCumleBileseni {

    Kolon kolon;

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
