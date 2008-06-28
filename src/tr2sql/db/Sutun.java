package tr2sql.db;

/**
 * veri tabani sutununu ifade eder.
 */

public class Sutun {

    private String ad;
    private Kavram kavram;
    private SutunTipi tip;
    private boolean anahtar = false;

// ---- constructor ----

    public Sutun(String ad, Kavram kavram, SutunTipi tip, boolean anahtar) {
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

    public SutunTipi getTip() {
        return tip;
    }

    public boolean isAnahtar() {
        return anahtar;
    }

    public String toString() {
        return "Sutun{" +
                "ad:'" + ad + '\'' +
                ", kavram:'" + kavram + '\'' +
                ", tip:" + tip +
                ", anahtar:" + anahtar +
                '}';
    }
}