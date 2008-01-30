package tr2sql.db;

import org.jmate.Files;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.List;

import net.zemberek.araclar.XmlYardimcisi;

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

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("VeriTabani:" + ad + '\n' + "Tablolar:\n");
        for (Tablo tablo : tablolar)
           b.append('\t').append(tablo.toString()).append('\n');
        return b.toString();
    }    
}
