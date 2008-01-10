package tr2sql;

import net.zemberek.tr.yapi.ek.TurkceEkAdlari;
import net.zemberek.yapi.DilBilgisi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.KelimeTipi;
import net.zemberek.yapi.Kok;
import net.zemberek.yapi.ek.Ek;
import net.zemberek.yapi.ek.EkYonetici;
import softekpr.helpers.Files;
import softekpr.helpers.Strings;
import softekpr.helpers.collections.Lists;
import softekpr.helpers.collections.Sets;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


public class Tr2SQLKelimeEleyici implements KelimeEleyici {

    private Logger logger = Logger.getAnonymousLogger();

    private DilBilgisi dilBilgisi;

    private Set<Kok> kokler = Sets.newHashSet();
    private Set<Ek> kisitlananEkler;

    public Tr2SQLKelimeEleyici(DilBilgisi dilBilgisi) {

        this.dilBilgisi = dilBilgisi;

        kokleriOku();

        EkYonetici ekler = dilBilgisi.ekler();
        kisitlananEkler = Sets.newHashSet(
                ekler.ek(TurkceEkAdlari.ISIM_ANDIRMA_IMSI),
                ekler.ek(TurkceEkAdlari.ISIM_ANDIRMA_SI),
                ekler.ek(TurkceEkAdlari.ISIM_DONUSUM_LES),
                ekler.ek(TurkceEkAdlari.ISIM_ILISKILI_SEL),
                ekler.ek(TurkceEkAdlari.ISIM_DONUSUM_LE),
                ekler.ek(TurkceEkAdlari.ISIM_KUCULTME_CEGIZ),
                ekler.ek(TurkceEkAdlari.ISIM_KUCULTME_CIK),
                ekler.ek(TurkceEkAdlari.FIIL_SURERLIK_EGOR),
                ekler.ek(TurkceEkAdlari.FIIL_DONUSUM_ESICE),
                ekler.ek(TurkceEkAdlari.FIIL_SURERLIK_EKAL)
        );

    }

    public List<Kelime> ele(Kelime... kelimeler) {
        List<Kelime> sonuc = Lists.newArrayList();
        for (Kelime kelime : kelimeler) {
            // eger kelime koku bizim listede yoksa hic kasma, donguye devam et.
            if (!this.kokler.contains(kelime.kok()))
                continue;

            // eger eklerden her hangi birisi uygunsuz ise
            boolean kisitliEkBulundu = false;
            for (Ek ek : kelime.ekler()) {
                if (this.kisitlananEkler.contains(ek)) {
                    kisitliEkBulundu = true;
                    break;
                }
            }
            if (!kisitliEkBulundu)
                sonuc.add(kelime);
        }
        return sonuc;
    }

    private void kokleriOku() {

        // bin/temel-kokler.txt dosyasindan kokleri okuyalim. Files helper sinifindan yararlaniliyor.
        List<String> konuOzelStringler =
                Files.readAsTrimmedStringList(Files.getBufferedReader("bilgi/temel-kokler.txt", "utf-8"));

        konuOzelStringler = gecersizSatrilariEle(konuOzelStringler);
        //
        for (String s : konuOzelStringler) {
            if (Strings.hasText(s)) {
                // elimizden geldigince kok Stringininn yazilisina gore gercekte hangi koke karsilik dustugunu buluyoruz.
                Kok k = kokTahminEt(s);
                if (k != null)
                    this.kokler.add(k);
                else {
                    Kok kok = yeniKok(s);
                    dilBilgisi.kokler().ekle(kok);
                    logger.info(s + " icin kok bulunamadi. yeni kok eklenecek:" + kok);
                    // burada kok zemberek icinde bulunamazsa dilbilgisi icerisindeki koklere eklenebilir.
                }
            }
        }
    }

    private List<String> gecersizSatrilariEle(List<String> list) {
        List<String> yeni = Lists.newArrayList();
        for (String s : list) {
            if (Strings.hasText(s) && !s.startsWith("#")) {
                yeni.add(s);
            }
        }
        return yeni;
    }

    private Kok kokTahminEt(String s) {
        Kok adayKok = null;
        // eger kelime "mek" ya da "mak" ile bitiyorsa ve depoda fiil olan kok varsa bunu dondur.
        if (fiilOlabilirMi(s))
            adayKok = dilBilgisi.kokler().kokBul(s.substring(0, s.length() - 3), KelimeTipi.FIIL);
        if (adayKok != null)
            return adayKok;

        // tek ko varsa depoda bu kelimeye karsilik dusen, dondur.
        List<Kok> kokler = dilBilgisi.kokler().kokBul(s);
        if (kokler.size() == 1)
            return kokler.get(0);

        //birden ock kok varsa ilk koku dondur.
        for (Kok kok : kokler) {
            if (kok.tip() != KelimeTipi.FIIL)
                return kok;
        }

        return null;

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
