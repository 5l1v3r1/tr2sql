package tr2sql.db;

import net.zemberek.yapi.Kok;

import java.util.List;

/**
 * Bu sinifta veri tabanina iliskin bilgiler yer alir.
 */
public class VeriTabani {

    private String ad;

    private List<Tablo> tablolar;

// ---- constructor ----

    public VeriTabani(String ad, List<Tablo> tablolar) {
        this.ad = ad;
        this.tablolar = tablolar;
    }

// ---- getters ----

    public String getAd() {
        return ad;
    }

    public List<Tablo> getTablolar() {
        return tablolar;
    }

    public Tablo kokeGoreTabloBul(Kok kok) {
        for (Tablo tablo : tablolar) {
            if (tablo.getKavram().kokMevcutMu(kok))
                return tablo;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("VeriTabani:" + ad + '\n' + "Tablolar:\n");
        for (Tablo tablo : tablolar)
            b.append('\t').append(tablo.toString()).append('\n');
        return b.toString();
    }
}
