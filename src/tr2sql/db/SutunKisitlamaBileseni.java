package tr2sql.db;

import tr2sql.cozumleyici.BilgiBileseni;

import java.util.List;
import java.util.ArrayList;

/**
 * Bu sinif sutun kisitlama verisini tasir. Ornegin "numarasi [5] olan" kelimelerinden
 * Sutun = NUMARA, kisitlamaDegeri="5" ve KiyasTipi=ESITLIK verileri ile bu siniftan bir nesne olusturulmasi gerekir.
 */
public class SutunKisitlamaBileseni {
    public Sutun sutun;
    public List<BilgiBileseni> kisitlamaBilgileri = new ArrayList<BilgiBileseni>();
    public BaglacTipi oncekiBilsenIliskisi = BaglacTipi.YOK;

    public SutunKisitlamaBileseni(Sutun sutun,
                                  List<BilgiBileseni> kisitlamaBilgileri,
                                  BaglacTipi oncekiBilsenIliskisi) {
        this.sutun = sutun;
        this.kisitlamaBilgileri = kisitlamaBilgileri;
        this.oncekiBilsenIliskisi = oncekiBilsenIliskisi;
    }

    public SutunKisitlamaBileseni(Sutun sutun,
                                  BilgiBileseni kisitlamaBilgisi,
                                  BaglacTipi oncekiBilsenIliskisi) {
        this.sutun = sutun;
        kisitlamaBilgileri.add(kisitlamaBilgisi);
        this.oncekiBilsenIliskisi = oncekiBilsenIliskisi;
    }

    public String sqlKisitlamaDegeriUret(BilgiBileseni bilgiBileseni) {

        KiyasTipi kiyasTipi = bilgiBileseni.getKiyasTipi();
        String kisitlamaDegeri = bilgiBileseni.icerik();

        if (sutun == null)
            throw new SQLUretimHatasi("Kisitlama bileseni icin sutun degeri null olamaz.");
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
            return sutun.getAd() + " " + deger + " " + kisitlamaDegeriniBicimlendir(kisitlamaDegeri) + " ";

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
            return sutun.getAd() + " " + deger + " ";

        if (kiyasTipi == KiyasTipi.NULL)
            return sutun.getAd() + " is null ";

        if (kiyasTipi == KiyasTipi.NULL_DEGIL)
            return sutun.getAd() + " is not null ";

        throw new SQLUretimHatasi("Kisitlama bileseni icin sql kelimeleri uretilemedi:" + toString());
    }


    public String sqlDonusumu() {

        if (kisitlamaBilgileri.isEmpty())
            return "";


        StringBuilder sb = new StringBuilder();

        if (kisitlamaBilgileri.size() == 1)
            sb.append(sqlKisitlamaDegeriUret(kisitlamaBilgileri.get(0)));
        else {
            sb.append("(");
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
        }
        switch (oncekiBilsenIliskisi) {
            case VE:
                return " and " + sb.toString();
            case VEYA:
                return " or " + sb.toString();
            case VIRGUL:
                return " and " + sb.toString();
        }

        return sb.toString() + " ";
    }

    private String kisitlamaDegeriniBicimlendir(String kisitlamaDegeri) {
        if (sutun.getTip() == SutunTipi.YAZI)
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
        return " Sutun : " + sutun.toString() + " , kisitlamaDegeri:" + kisitlamaBilgileri.toString();
    }

}
