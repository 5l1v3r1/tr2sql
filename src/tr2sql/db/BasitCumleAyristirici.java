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

        // bu regular expression ile 
        Pattern parcalayici = Pattern.compile("('[^']*')|[^ \\t\\n,.]+|,");
        Matcher m = parcalayici.matcher(c);

        while (m.find())
            parcalar.add(m.group());

        return parcalar;
    }

    public static void main(String[] args) {
       List<String> parcalar = new BasitCumleAyristirici("Mini mini bir 'kuş lar' donmuştu, pencereme konmuştu...").ayristir();
        System.out.println("parcalar = " + parcalar); 
    }
}
