package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.BaglacTipi;
import tr2sql.db.Sutun;

/**
 * dilek
 */
public class SutunBileseni extends CumleBileseni {

    Sutun sutun;
    BaglacTipi onBaglac = BaglacTipi.YOK;

    public SutunBileseni(Sutun sutun, Kelime kelime) {
        this.sutun = sutun;
        this.tip = CumleBilesenTipi.SUTUN;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;
    }

    public void setOnBaglac(BaglacTipi onBaglac) {
        this.onBaglac = onBaglac;
    }

    public boolean baglacVar() {
        return onBaglac != BaglacTipi.YOK;
    }

    public Sutun getSutun() {
        return sutun;
    }

    public BaglacTipi getOnBaglac() {
        return onBaglac;
    }

    public String toString() {
        return "[" + icerik + ":" + tip() + (onBaglac == BaglacTipi.YOK ? "" : (": on baglac=" + onBaglac.name())) + "] ";
    }
}
