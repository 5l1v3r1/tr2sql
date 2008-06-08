package tr2sql.cozumleyici;

import tr2sql.db.BaglacTipi;
import tr2sql.db.KiyasTipi;

public class BilgiBileseni extends CumleBileseni {

    BaglacTipi onBaglac = BaglacTipi.YOK;
    KiyasTipi kiyasTipi = KiyasTipi.ESIT;

    public BilgiBileseni(String icerik) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
    }

    public BilgiBileseni(String icerik, KiyasTipi kiyasTipi) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
        this.kiyasTipi = kiyasTipi;
    }

    public BaglacTipi getOnBaglac() {
        return onBaglac;
    }

    public void setOnBaglac(BaglacTipi onBaglac) {
        this.onBaglac = onBaglac;
    }

    public boolean baglacVar() {
        return onBaglac != BaglacTipi.YOK;
    }

    public KiyasTipi getKiyasTipi() {
        return kiyasTipi;
    }

    public void setKiyasTipi(KiyasTipi kiyasTipi) {
        this.kiyasTipi = kiyasTipi;
    }

    public String toString() {
        return "[" + icerik + ":" + tip() + (onBaglac==BaglacTipi.YOK ? "" : (": on baglac=" + onBaglac.name()))+"] ";
    }
    
}
