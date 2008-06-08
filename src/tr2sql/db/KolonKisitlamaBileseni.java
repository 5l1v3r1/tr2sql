package tr2sql.db;

import tr2sql.cozumleyici.BilgiBileseni;

import java.util.List;
import java.util.ArrayList;

/**
 * Bu sinif kolon kisitlama verisini tasir. Ornegin "numarasi [5] olan" kelimelerinden
 * Kolon = NUMARA, kisitlamaDegeri="5" ve KiyasTipi=ESITLIK verileri ile bu siniftan bir nesne olusturulmasi gerekir.
 */
public class KolonKisitlamaBileseni {
    public Kolon kolon;
    public List<BilgiBileseni> kisitlamaBilgileri = new ArrayList<BilgiBileseni>();

    public KolonKisitlamaBileseni(Kolon kolon, List<BilgiBileseni> kisitlamaBilgileri) {
        this.kolon = kolon;
        this.kisitlamaBilgileri = kisitlamaBilgileri;
    }

    public KolonKisitlamaBileseni(Kolon kolon, BilgiBileseni kisitlamaBilgisi) {
        this.kolon = kolon;
        kisitlamaBilgileri.add(kisitlamaBilgisi);
    }

    public String sqlKisitlamaDegeriUret(BilgiBileseni bilgiBileseni) {

        KiyasTipi kiyasTipi = bilgiBileseni.getKiyasTipi();
        String kisitlamaDegeri = bilgiBileseni.icerik();

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
            return kolon.getAd() + " " + deger + " " + kisitlamaDegeriniBicimlendir(kisitlamaDegeri) + " ";

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


    public String sqlDonusumu() {

        if (kisitlamaBilgileri.isEmpty())
            return "";

        if (kisitlamaBilgileri.size() == 1)
            return sqlKisitlamaDegeriUret(kisitlamaBilgileri.get(0));

        StringBuilder sb = new StringBuilder("(");
        for (BilgiBileseni bilgiBileseni : kisitlamaBilgileri) {
            switch (bilgiBileseni.getOnBaglac()) {
                case VE:
                    sb.append(" and ");
                    break;
                case VIRGUL:
                case VEYA:
                    sb.append(" or ");
                    break;
                case YOK:
                    break;
            }
            sb.append(sqlKisitlamaDegeriUret(bilgiBileseni));
        }
        sb.append(")");
        return sb.toString();
    }

    private String kisitlamaDegeriniBicimlendir(String kisitlamaDegeri) {
        if (kolon.getTip() == KolonTipi.YAZI)
            return "'" + kisitlamaDegeri + "'";
        else return kisitlamaDegeri;
    }

    public void kiyasTipiniTersineCevir() {
        for (BilgiBileseni bilgiBileseni : kisitlamaBilgileri) {
            bilgiBileseni.setKiyasTipi(bilgiBileseni.getKiyasTipi().tersi());
        }
    }

    @Override
    public String toString() {
        return " Kolon : " + kolon.toString() + " , kisitlamaDegeri:" + kisitlamaBilgileri.toString();
    }

}
