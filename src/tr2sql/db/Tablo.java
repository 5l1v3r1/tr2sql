package tr2sql.db;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * veri tabani tablosunu ifade eder.
 */
@XStreamAlias(value = "tablo")
public class Tablo {

    @XStreamAsAttribute
    private String ad;

    @XStreamAsAttribute
    private String kavram;

    private List<Kolon> kolonlar = new ArrayList<Kolon>();

// ---- constructor ----

    public Tablo(String ad, String kavram, List<Kolon> kolonlar) {
        this.ad = ad;
        this.kavram = kavram;
        this.kolonlar = kolonlar;
    }

// ---- getters ----

    public String getAd() {
        return ad;
    }

    public String getKavram() {
        return kavram;
    }

    public List<Kolon> getKolonlar() {
        return kolonlar;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Tablo:" + ad + '\n' + "Kolonlar:\n");
        for (Kolon kolon : kolonlar)
           b.append('\t').append(kolon.toString()).append('\n');
        return b.toString();

    }
}
