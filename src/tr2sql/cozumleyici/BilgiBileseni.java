package tr2sql.cozumleyici;

public class BilgiBileseni extends TemelCumleBileseni {

    public BilgiBileseni(String icerik) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
    }

}
