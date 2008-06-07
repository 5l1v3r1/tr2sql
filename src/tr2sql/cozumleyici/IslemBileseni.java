package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;
import tr2sql.db.IslemTipi;

public class IslemBileseni extends CumleBileseni {

    IslemTipi islem;
    boolean olumsuzluk;

    public IslemBileseni(IslemTipi islem, Kelime kelime) {
        this.islem = islem;
        this.tip = CumleBilesenTipi.ISLEM;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;
    }

    public IslemTipi getIslem() {
        return islem;
    }

    public boolean olumsuz() {
        return olumsuzluk;
    }

    public void setOlumsuzluk(boolean olumsuzluk) {
        this.olumsuzluk = olumsuzluk;
    }
}
