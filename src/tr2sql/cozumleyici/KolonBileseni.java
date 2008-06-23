package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.BaglacTipi;
import tr2sql.db.Kolon;

/**
 * dilek
 */
public class KolonBileseni extends CumleBileseni {

    Kolon kolon;
    BaglacTipi onBaglac = BaglacTipi.YOK;

    public KolonBileseni(Kolon kolon, Kelime kelime) {
        this.kolon = kolon;
        this.tip = CumleBilesenTipi.KOLON;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;
    }

    public void setOnBaglac(BaglacTipi onBaglac) {
        this.onBaglac = onBaglac;
    }

    public boolean baglacVar() {
        return onBaglac != BaglacTipi.YOK;
    }

    public Kolon getKolon() {
        return kolon;
    }

    public BaglacTipi getOnBaglac() {
        return onBaglac;
    }

    public String toString() {
        return "[" + icerik + ":" + tip() + (onBaglac == BaglacTipi.YOK ? "" : (": on baglac=" + onBaglac.name())) + "] ";
    }
}
