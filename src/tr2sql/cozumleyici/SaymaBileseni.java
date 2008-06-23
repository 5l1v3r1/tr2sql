package tr2sql.cozumleyici;

public class SaymaBileseni extends CumleBileseni{

    public SaymaBileseni(String icerik) {
        this.icerik = icerik;
        this.tip = CumleBilesenTipi.SAY;
    }
}