package tr2sql.cozumleyici;

import tr2sql.db.Kolon;

public class KisitlamaBileseni implements SorguCumleBileseni {

    private String deger;
    private Kolon kolon;

    public KisitlamaBileseni(String deger) {
        this.deger = deger;
    }

    

    public String getDeger() {
        return deger;
    }

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.KISITLAMA_BILGISI;
    }
}
