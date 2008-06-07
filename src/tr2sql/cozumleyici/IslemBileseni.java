package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.IslemTipi;

public class IslemBileseni extends TemelCumleBileseni {

    IslemTipi islem;

    public IslemBileseni(IslemTipi islem, Kelime kelime) {
        this.islem = islem;
        this.tip = CumleBilesenTipi.ISLEM;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;
    }

    public IslemTipi getIslem() {
        return islem;
    }
}
