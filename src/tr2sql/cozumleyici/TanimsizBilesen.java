package tr2sql.cozumleyici;

public class TanimsizBilesen extends TemelCumleBileseni {

    public TanimsizBilesen(String icerik) {
        this.icerik = icerik;
        this.tip = CumleBilesenTipi.TANIMSIZ;
    }
}
