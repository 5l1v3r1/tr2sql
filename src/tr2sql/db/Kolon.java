package tr2sql.db;

/**
 * veri tabani kolonunu ifade eder.
 */

public class Kolon {

    private String ad;
    private Kavram kavram;
    private KolonTipi tip;
    private boolean anahtar = false;

// ---- constructor ----

    public Kolon(String ad, Kavram kavram, KolonTipi tip, boolean anahtar) {
        this.ad = ad;
        this.kavram = kavram;
        this.tip = tip;
        this.anahtar = anahtar;
    }

// ---- getters ----

    public String getAd() {
        return ad;
    }

    public Kavram getKavram() {
        return kavram;
    }

    public KolonTipi getTip() {
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
