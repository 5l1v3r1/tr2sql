package tr2sql.cozumleyici;

import tr2sql.db.KiyasTipi;

public class KiyaslamaBileseni extends TemelCumleBileseni {
    KiyasTipi kiyasTipi;
    boolean olumsuzuk;

    public KiyaslamaBileseni(KiyasTipi kiyasTipi, boolean olumsuzuk) {
        this.kiyasTipi = kiyasTipi;
        this.olumsuzuk = olumsuzuk;
    }
}
