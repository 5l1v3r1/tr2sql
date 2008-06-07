package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.Tablo;

public class TabloBileseni extends CumleBileseni {

    Tablo tablo;

    public TabloBileseni(Tablo tablo, Kelime kelime) {
        this.tablo = tablo;
        this.tip = CumleBilesenTipi.TABLO;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;
    }

    public Tablo getTablo() {
        return tablo;
    }
}
