package tr2sql.db;

import net.zemberek.yapi.Kok;

import java.util.ArrayList;
import java.util.List;

/**
 * veri tabani tablosunu ifade eder.
 */
public class Tablo {

    private String ad;
    private Kavram kavram;

    private List<Sutun> sutunlar = new ArrayList<Sutun>();

// ---- constructor ----

    public Tablo(String ad, Kavram kavram, List<Sutun> sutunlar) {
        this.ad = ad;
        this.kavram = kavram;
        this.sutunlar = sutunlar;
    }

// ---- getters ----

    public String getAd() {
        return ad;
    }

    public Kavram getKavram() {
        return kavram;
    }

    public List<Sutun> getSutunlar() {
        return sutunlar;
    }

    // ---- diger metodlar -----

    /**
     * kok'e uygun sutun bulursa dondurur.
     *
     * @param kok : kelime koku
     * @return : eger kok sutun kavrami dahilinde ise ilk bulunan sutun doner. hicbir sutun kavrami ile
     *         uyusmazsa null doner.
     */
    public Sutun kokeGoreSutunBul(Kok kok) {
        for (Sutun sutun : sutunlar) {
            if (sutun.getKavram().kokMevcutMu(kok))
                return sutun;
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Tablo:" + ad + ", kavram:" + kavram + '\n' + "Sutunlar:\n");
        for (Sutun sutun : sutunlar)
            b.append('\t').append(sutun.toString()).append('\n');
        return b.toString();

    }
}
