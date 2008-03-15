package tr2sql;

import net.zemberek.bilgi.kokler.Sozluk;
import net.zemberek.yapi.KelimeTipi;
import net.zemberek.yapi.Kok;

import java.util.List;


/**
 * sozlukten kok tahmin etme ve sozluge bir stringi tahmin edip Kok olarak ekleme
 * islemlerini yapar.
 */
public class SozlukIslemleri {

    private Sozluk sozluk;

    public SozlukIslemleri(Sozluk sozluk) {
        this.sozluk = sozluk;
    }

    /**
     * herhangi bir Stringin hangi koke karsilik dustugunu tahmin etmeye calisir.
     * eger kelime -mek, -mak ile bitiyorsa fiil olarak sozlukte arama yapar.
     * eger bulunmazsa ya da -mastar ile bitmiyorsa gelen sonuclardan fiil olmayan ilk elemani dondurur.
     *
     * @param s kok'u ifade eden String.
     * @return tahmin edilen koku dondurur.
     */
    public Kok kokTahminEt(String s) {
        Kok adayKok = null;
        // eger kelime "mek" ya da "mak" ile bitiyorsa ve depoda fiil olan kok varsa bunu dondur.
        if (fiilOlabilirMi(s))
            adayKok = sozluk.kokBul(s.substring(0, s.length() - 3), KelimeTipi.FIIL);
        if (adayKok != null)
            return adayKok;

        // tek ko varsa depoda bu kelimeye karsilik dusen, dondur.
        List<Kok> kokler = sozluk.kokBul(s);
        if (kokler.size() == 1)
            return kokler.get(0);

        //birden ock kok varsa ilk koku dondur.
        for (Kok kok : kokler) {
            if (kok.tip() != KelimeTipi.FIIL)
                return kok;
        }

        return null;

    }

    /**
     * Bir stringin tipini tahmin etmeye calisir ve Kok nesnesi olarak kok agacina ekler.
     *
     * @param s koku ifade eden String.
     * @return olusturulan ve agaca eklenen yeni kok.
     */
    public Kok tahminEtVeEkle(String s) {
        Kok kok = yeniKok(s);
        sozluk.ekle(kok);
        return kok;
    }

    private Kok yeniKok(String s) {
        // fiil gibi ise "mek" "mak" mastarini silip ekle. yoksa isim olarak ekle.
        if (fiilOlabilirMi(s))
            return new Kok(s.substring(0, s.length() - 3), KelimeTipi.FIIL);
        else
            return new Kok(s, KelimeTipi.ISIM);
    }

    private boolean fiilOlabilirMi(String s) {
        return s.endsWith("mek") || s.endsWith("mak");
    }


}
