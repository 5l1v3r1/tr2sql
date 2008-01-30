package tr2sql.db;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.jmate.Files;

import java.io.IOException;
import java.util.List;

/**
 * Bu sinifta veri tabanina iliskin bilgiler yer alir.
 */
@XStreamAlias(value = "veri-tabani")
public class VeriTabani {

    @XStreamAsAttribute
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

    public static void main(String[] args) throws IOException {
        XStream xStream = new XStream();
        Annotations.configureAliases(xStream, VeriTabani.class, Tablo.class, Kolon.class, KolonTipi.class);
        VeriTabani vt = (VeriTabani) xStream.fromXML(Files.getReader("bilgi/basit-veri-tabani.xml", "UTF-8"));
        System.out.println("vt = " + vt);
    }
}
