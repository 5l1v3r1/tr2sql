package tr2sql.cozumleyici;

import tr2sql.db.BaglacTipi;

public class BilgiBileseni extends CumleBileseni {

    BaglacTipi onBaglac = BaglacTipi.YOK;

    public BilgiBileseni(String icerik) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
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
}
