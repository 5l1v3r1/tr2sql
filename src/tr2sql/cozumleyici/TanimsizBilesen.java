package tr2sql.cozumleyici;

public class TanimsizBilesen extends CumleBileseni {

    public TanimsizBilesen(String icerik) {
        this.icerik = icerik;
        this.tip = CumleBilesenTipi.TANIMSIZ;
    }
}
