package tr2sql.cozumleyici;

import net.zemberek.yapi.Kelime;

public class TemelCumleBileseni implements SorguCumleBileseni {
    protected CumleBilesenTipi tip;
    protected String icerik;
    protected Kelime kelime;

    public CumleBilesenTipi tip() {
        return tip;
    }

    public String icerik() {
        return icerik;
    }
}
