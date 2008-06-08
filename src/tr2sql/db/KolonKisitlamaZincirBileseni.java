package tr2sql.db;

/**
 * Bu sinif iki kolon kisitlama bileseninin nasil bir kosul ile baglandigini gosterir.
 * bilesen1 AND bilesen2 gibi.
 */
public class KolonKisitlamaZincirBileseni {
    public KolonKisitlamaBileseni kisitlamaBileseni;
    public BaglacTipi oncekiBilsenIliskisi = BaglacTipi.YOK;

    public KolonKisitlamaZincirBileseni(KolonKisitlamaBileseni kisitlamaBileseni,
                                        BaglacTipi oncekiBilsenIliskisi) {
        this.kisitlamaBileseni = kisitlamaBileseni;
        this.oncekiBilsenIliskisi = oncekiBilsenIliskisi;
    }

    public String sqlDonusumu() {
        if (kisitlamaBileseni == null)
            throw new SQLUretimHatasi("Kisitlama bileseni null olamaz.");

        String kisitlamaSql = kisitlamaBileseni.sqlDonusumu();

        switch (oncekiBilsenIliskisi) {
            case VE:
                return " and " + kisitlamaSql;
            case VEYA:
                return " or " + kisitlamaSql ;
            case VIRGUL:
                return " and " + kisitlamaSql;
        }
        return kisitlamaSql + " ";
    }
}
