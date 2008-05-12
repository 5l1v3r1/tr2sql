package tr2sql.cozumleyici;

import tr2sql.db.Kolon;

/**
 * dilek
 */
public class KolonBileseni implements SorguCumleBileseni{

    Kolon kolon;
    String icerik;

    public KolonBileseni(Kolon kolon, String icerik) {
        this.kolon = kolon;
        this.icerik = icerik;
    }

    public Kolon getKolon() {
        return kolon;
    }

    public String getIcerik() {
        return icerik;
    }

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.KOLON;        
    }

    public String icerik() {
        return icerik;
    }
}
