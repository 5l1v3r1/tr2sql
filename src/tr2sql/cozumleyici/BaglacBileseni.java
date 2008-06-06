package tr2sql.cozumleyici;

import tr2sql.db.KosulTipi;


/**
 * ve, veya ve virgul kavramlarini tasir.
 */
public class BaglacBileseni extends TemelCumleBileseni {

    KosulTipi kosulTipi = KosulTipi.VE;

    public BaglacBileseni(String icerik) {
        this.icerik = icerik;
        this.tip = CumleBilesenTipi.BAGLAC;        
    }
}
