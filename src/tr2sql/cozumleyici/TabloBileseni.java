package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.Tablo;

public class TabloBileseni implements SorguCumleBileseni {

    Tablo tablo;
    String icerik;

    public TabloBileseni(Tablo tablo, String icerik) {
        this.tablo = tablo;
        this.icerik = icerik;
    }

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.TABLO;
    }

    public String icerik() {
        return icerik;
    }
}
