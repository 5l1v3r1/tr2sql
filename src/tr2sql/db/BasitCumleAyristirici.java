package tr2sql.db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Bu sinif gelen bir cumleyi en basit anlamda kavram, bilgi gibi parcalara ayririr. *
 */
public class BasitCumleAyristirici {
    final String cumle;

    public BasitCumleAyristirici(String icerik) {
        this.cumle = icerik;
    }

    List<String> ayristir() {
        List<String> parcalar = new ArrayList<String>();
        String c = cumle.replaceAll("[ ]+", " ").trim();

        Pattern parcalayici = Pattern.compile("[a-zA-ZçğıöşüÇİĞÖŞÜ']+[^\\s.,]|[,]");
        Matcher m = parcalayici.matcher(c);

        while (m.find())
            parcalar.add(m.group());

        return parcalar;
    }

    public static void main(String[] args) {
        new BasitCumleAyristirici("Mini mini bir 'kuş' donmuştu, pencereme konmuştu...").ayristir();
    }
}
