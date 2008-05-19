package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;

public class TanimsizBilesen extends TemelCumleBileseni {

    public TanimsizBilesen(String icerik) {
        this.icerik = icerik;
        this.tip = CumleBilesenTipi.TANIMSIZ;
    }
}
