package tr2sql.cozumleyici;

public class MiktarKisitlamaBileseni extends CumleBileseni {
    public MiktarKisitlamaBileseni(String s) {
        this.tip = CumleBilesenTipi.SONUC_MIKTAR;
        this.icerik = s;
    }
}
