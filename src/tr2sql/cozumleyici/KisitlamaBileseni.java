package tr2sql.cozumleyici;

import tr2sql.db.Kolon;

public class KisitlamaBileseni implements SorguCumleBileseni {

    private String icerik;
    private Kolon kolon;

    public KisitlamaBileseni(String icerik) {
        this.icerik = icerik;
    }

    public String getIcerik() {
        return icerik;
    }

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.KISITLAMA_BILGISI;
    }

    public String icerik() {
        return icerik;
    }
}
