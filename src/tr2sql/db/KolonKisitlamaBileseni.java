package tr2sql.db;

/**
 * Bu sinif kolon kisitlama verisini tasir. Ornegin "numarasi [5] olan" kelimelerinden
 * Kolon = NUMARA, kisitlamaDegeri="5" ve KiyasTipi=ESITLIK verileri ile bu siniftan bir nesne olusturulmasi gerekir.
 */
public class KolonKisitlamaBileseni {
    public Kolon kolon;

    public String kisitlamaDegeri;
    public KiyasTipi kiyasTipi = KiyasTipi.ESIT;

    public KolonKisitlamaBileseni(Kolon kolon, String kisitlamaDegeri, KiyasTipi kiyasTipi) {
        this.kolon = kolon;
        this.kisitlamaDegeri = kisitlamaDegeri;
        this.kiyasTipi = kiyasTipi;
    }

    public String sqlDonusumu() {
        if (kolon == null)
            throw new SQLUretimHatasi("Kisitlama bileseni icin kolon degeri null olamaz.");
        if (kiyasTipi == null)
            throw new SQLUretimHatasi("Kisitlama bileseni icin kiyasTipi null olamaz.");

        // kiyas tipine gore sonucu tamamlayalim.
        boolean matematikselKiyas = true;
        String deger = "";
        switch (kiyasTipi) {
            case BUYUK:
                deger = ">";
                break;
            case KUCUK:
                deger = "<";
                break;
            case BUYUK_ESIT:
                deger = ">=";
                break;
            case KUCUK_ESIT:
                deger = "<=";
                break;
            case ESIT:
                deger = "=";
                break;
            case ESIT_DEGIL:
                deger = "<>";
                break;
            default:
                matematikselKiyas = false;
                break;
        }
        if (matematikselKiyas)
            return kolon.getAd() + " " + deger + " " + kisitlamaDegeriniBicimlendir() + " ";

        boolean benzerlikKiyaslama = true;
        switch (kiyasTipi) {
            case BASI_BENZER:
                deger = "like '%" + kisitlamaDegeri + "'";
                break;
            case BASI_BENZEMEZ:
                deger = "not like '%" + kisitlamaDegeri + "'";
                break;
            case SONU_BENZER:
                deger = "like '" + kisitlamaDegeri + "%'";
                break;
            case SONU_BENZEMEZ:
                deger = "not like '" + kisitlamaDegeri + "%'";
                break;
            case ARA_BENZER:
                deger = "like '%" + kisitlamaDegeri + "%'";
                break;
            case ARA_BENZEMEZ:
                deger = "not like '%" + kisitlamaDegeri + "%'";
                break;
            default:
                benzerlikKiyaslama = false;
                break;
        }
        if (benzerlikKiyaslama)
            return kolon.getAd() + " " + deger + " ";

        if (kiyasTipi == KiyasTipi.NULL)
            return kolon.getAd() + " is null ";

        if (kiyasTipi == KiyasTipi.NULL_DEGIL)
            return kolon.getAd() + " is not null ";

        throw new SQLUretimHatasi("Kisitlama bileseni icin sql kelimeleri uretilemedi:" + toString());

    }

    private String kisitlamaDegeriniBicimlendir() {
        if (kolon.getTip() == KolonTipi.YAZI)
            return "'" + kisitlamaDegeri + "'";
        else return kisitlamaDegeri;
    }

    public void kiyasTipiniTersineCevir() {
        kiyasTipi = kiyasTipi.tersi();
    }

    @Override
    public String toString() {
        return " Kolon : " + kolon.toString() + " , kisitlamaDegeri:" + kisitlamaDegeri + ", " +
                " Kiyas Tipi:" + kiyasTipi.name();
    }

}
