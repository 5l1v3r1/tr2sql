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

    private List<Kolon> kolonlar = new ArrayList<Kolon>();

// ---- constructor ----

    public Tablo(String ad, Kavram kavram, List<Kolon> kolonlar) {
        this.ad = ad;
        this.kavram = kavram;
        this.kolonlar = kolonlar;
    }

// ---- getters ----

    public String getAd() {
        return ad;
    }

    public Kavram getKavram() {
        return kavram;
    }

    public List<Kolon> getKolonlar() {
        return kolonlar;
    }

    // ---- diger metodlar -----

    /**
     * kok'e uygun kolon bulursa dondurur.
     *
     * @param kok : kelime koku
     * @return : eger kok kolon kavrami dahilinde ise ilk bulunan kolon doner. hicbir kolon kavrami ile
     *         uyusmazsa null doner.
     */
    public Kolon kokeGoreKolonBul(Kok kok) {
        for (Kolon kolon : kolonlar) {
            if (kolon.getKavram().kokMevcutMu(kok))
                return kolon;
        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Tablo:" + ad + ", kavram:" + kavram + '\n' + "Kolonlar:\n");
        for (Kolon kolon : kolonlar)
            b.append('\t').append(kolon.toString()).append('\n');
        return b.toString();

    }
}
