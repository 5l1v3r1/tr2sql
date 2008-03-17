package tr2sql.db;


/**
 * Bu sinif iki kolon kisitlama bileseninin nasil bir kosul ile baglandigini gosterir. bilesen1 AND bilesen2 gibi.
 */
public class KolonKisitlamaZincirBileseni {
    public KolonKisitlamaBileseni kisitlamaBileseni;
    public KosulTipi sonrakiBilsenIliskisi;

    public KolonKisitlamaZincirBileseni(KolonKisitlamaBileseni kisitlamaBileseni, KosulTipi sonrakiBilsenIliskisi) {
        this.kisitlamaBileseni = kisitlamaBileseni;
        this.sonrakiBilsenIliskisi = sonrakiBilsenIliskisi;
    }
}
