package tr2sql.cozumleyici;

public class SayiBileseni extends TemelCumleBileseni {

    int deger;

    public SayiBileseni(int deger) {
        this.deger = deger;
        this.tip = CumleBilesenTipi.SAYI;
        this.icerik = String.valueOf(deger);
    }
}
