package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;

/**
 * olmak fiilini tutar.
 */
public class OlmakBIleseni extends CumleBileseni {
    private boolean olumsuz;

    public OlmakBIleseni(Kelime kelime, boolean olumsuz) {
        this.olumsuz = olumsuz;
        this.tip = CumleBilesenTipi.OLMAK;
        this.kelime = kelime;
        this.icerik = kelime.icerikStr();
    }

    public boolean olumsuz() {
        return olumsuz;
    }
}
