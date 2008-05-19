package tr2sql.cozumleyici;

public class KisitlamaBileseni extends TemelCumleBileseni {

    public KisitlamaBileseni(String icerik) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
    }
}
