package tr2sql.cozumleyici;

import tr2sql.db.KiyasTipi;
import net.zemberek.yapi.Kelime;

public class KiyaslamaBileseni extends CumleBileseni {
    KiyasTipi kiyasTipi;
    boolean olumsuzuk;

    public KiyaslamaBileseni(KiyasTipi kiyasTipi, Kelime kelime, boolean olumsuzuk) {
        this.tip = CumleBilesenTipi.KIYASLAYICI;
        this.kiyasTipi = kiyasTipi;
        this.olumsuzuk = olumsuzuk;
        this.kelime = kelime;
        this.icerik = kelime.icerikStr();
    }
}
