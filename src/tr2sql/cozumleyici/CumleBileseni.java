package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;

public class CumleBileseni {
    protected CumleBilesenTipi tip;
    protected String icerik;
    protected Kelime kelime;

    public CumleBilesenTipi tip() {
        return tip;
    }

    public String icerik() {
        return icerik;
    }

    @Override
    public String toString() {
        return "[" + icerik + ":" + tip() + "] ";
    }
}
