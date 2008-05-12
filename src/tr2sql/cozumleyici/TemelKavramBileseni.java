package tr2sql.cozumleyici;

/**
 * Created by IntelliJ IDEA.
 * User: ahmetaa
 * Date: May 11, 2008
 * Time: 9:15:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class TemelKavramBileseni implements SorguCumleBileseni{

    CumleBilesenTipi tip;
    String icerik;

    public TemelKavramBileseni(CumleBilesenTipi tip, String icerik) {
        this.tip = tip;
        this.icerik = icerik;
    }

    public CumleBilesenTipi tip() {
        return tip;
    }

    public String icerik() {
        return icerik;
    }
}
