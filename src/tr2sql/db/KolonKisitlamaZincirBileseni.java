package tr2sql.db;

/**
 * Bu sinif iki kolon kisitlama bileseninin nasil bir kosul ile baglandigini gosterir.
 * bilesen1 AND bilesen2 gibi.
 */
public class KolonKisitlamaZincirBileseni {
    public KolonKisitlamaBileseni kisitlamaBileseni;
    public KosulTipi sonrakiBilsenIliskisi = KosulTipi.YOK;

    public KolonKisitlamaZincirBileseni(KolonKisitlamaBileseni kisitlamaBileseni,
                                        KosulTipi sonrakiBilsenIliskisi) {
        this.kisitlamaBileseni = kisitlamaBileseni;
        this.sonrakiBilsenIliskisi = sonrakiBilsenIliskisi;
    }

    public String sqlDonusumu() {
        if (kisitlamaBileseni == null)
            throw new SQLUretimHatasi("Kisitlama bileseni null olamaz.");

        String kisitlamaSql = kisitlamaBileseni.sqlDonusumu();

        switch (sonrakiBilsenIliskisi) {
            case VE:
                return kisitlamaSql + " and ";
            case VEYA:
                return kisitlamaSql + " or ";
        }
        return kisitlamaSql + " ";
    }
}
