package tr2sql.db;


public class KolonKisitlamaZincirBileseni {
    public KolonKisitlamaBileseni kisitlamaBileseni;
    public KosulTipi sonrakiBilsenIliskisi;

    public KolonKisitlamaZincirBileseni(KolonKisitlamaBileseni kisitlamaBileseni, KosulTipi sonrakiBilsenIliskisi) {
        this.kisitlamaBileseni = kisitlamaBileseni;
        this.sonrakiBilsenIliskisi = sonrakiBilsenIliskisi;
    }
}
