package tr2sql.cozumleyici;

import tr2sql.db.BaglacTipi;
import tr2sql.db.KiyasTipi;

public class BilgiBileseni extends CumleBileseni {

    BaglacTipi onBaglac = BaglacTipi.YOK;
    KiyasTipi kiyas = KiyasTipi.ESIT;

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

    public KiyasTipi getKiyas() {
        return kiyas;
    }

    public void setKiyas(KiyasTipi kiyas) {
        this.kiyas = kiyas;
    }
}
