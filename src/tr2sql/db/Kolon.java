package tr2sql.db;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * veri tabani kolonunu ifade eder.
 */
@XStreamAlias(value = "kolon")
public class Kolon {

    @XStreamAsAttribute
    private String ad;

    @XStreamAsAttribute
    private String kavram;

    @XStreamAsAttribute
    private String tip;

    @XStreamAsAttribute    
    private boolean anahtar = false;

// ---- constructor ----

    public Kolon(String ad, String kavram, String tip, boolean anahtar) {
        this.ad = ad;
        this.kavram = kavram;
        this.tip = tip;
        this.anahtar = anahtar;
    }

// ---- getters ----

    public String getAd() {
        return ad;
    }

    public String getKavram() {
        return kavram;
    }

    public String getTip() {
        return tip;
    }

    public boolean isAnahtar() {
        return anahtar;
    }

    public String toString() {
        return "Kolon{" +
                "ad:'" + ad + '\'' +
                ", kavram:'" + kavram + '\'' +
                ", tip:" + tip +
                ", anahtar:" + anahtar +
                '}';
    }
}
