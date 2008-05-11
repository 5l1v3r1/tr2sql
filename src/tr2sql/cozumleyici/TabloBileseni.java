package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.Tablo;

public class TabloBileseni implements SorguCumleBileseni {

    Tablo tablo;
    Kelime kelime;

    

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.TABLO;
    }
}
