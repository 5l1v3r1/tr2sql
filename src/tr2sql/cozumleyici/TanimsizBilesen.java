package tr2sql.cozumleyici;

public class TanimsizBilesen implements SorguCumleBileseni {

    public String icerik;

    public TanimsizBilesen(String icerik) {
        this.icerik = icerik;
    }

    public CumleBilesenTipi tip() {
        return CumleBilesenTipi.TANIMSIZ;
    }
}
