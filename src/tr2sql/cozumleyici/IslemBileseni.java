package tr2sql.cozumleyici;

import tr2sql.db.IslemTipi;

public class IslemBileseni implements SorguCumleBileseni{

    IslemTipi islem;
    String icerik;

    public IslemBileseni(IslemTipi islem, String icerik) {
        this.islem = islem;
        this.icerik = icerik;
    }

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.ISLEM;
    }

    public String icerik() {
        return icerik;
    }
}
