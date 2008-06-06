package tr2sql.cozumleyici;

import tr2sql.db.KiyasTipi;
import tr2sql.db.Kavram;

public class BilgiBileseni extends TemelCumleBileseni {

    KiyasTipi kiyasTipi = KiyasTipi.ESIT;

    public BilgiBileseni(String icerik) {
        this.tip = CumleBilesenTipi.KISITLAMA_BILGISI;
        this.icerik = icerik;
    }

    public void setKiyasTipi(KiyasTipi kiyasTipi) {
        this.kiyasTipi = kiyasTipi;
    }

    public KiyasTipi getKiyasTipi() {
        return kiyasTipi;
    }

    public boolean kiyasTipiBelirle(Kavram kavram) {
        String ad = kavram.getAd();
        if (ad.equals("BUYUK"))
            kiyasTipi = KiyasTipi.BUYUK;
        else if (ad.equals("KUCUK"))
            kiyasTipi = KiyasTipi.KUCUK;
        else if (ad.equals("ESIT"))
            kiyasTipi = KiyasTipi.ESIT;
        else if (ad.equals("ESIT_DEGIL"))
            kiyasTipi = KiyasTipi.ESIT_DEGIL;
        else if (ad.equals("BASI_BENZER"))
            kiyasTipi = KiyasTipi.BASI_BENZER;
        else if (ad.equals("SONU_BENZER"))
            kiyasTipi = KiyasTipi.SONU_BENZER;
        else if (ad.equals("NULL"))
            kiyasTipi = KiyasTipi.NULL;
        else return false;
        return true;
    }

}
