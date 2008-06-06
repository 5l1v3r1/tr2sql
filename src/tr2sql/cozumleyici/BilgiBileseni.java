package tr2sql.cozumleyici;

import tr2sql.db.KiyasTipi;
import tr2sql.db.Kavram;

public class BilgiBileseni extends TemelCumleBileseni {

    public BilgiBileseni(String icerik) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
    }

}
