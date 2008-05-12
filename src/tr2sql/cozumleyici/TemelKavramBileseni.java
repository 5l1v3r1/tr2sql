package tr2sql.cozumleyici;

/**
 * Bu bilesen genel olarak fazladan bilgi tasimayan bilesenler icin kullanilir.
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
