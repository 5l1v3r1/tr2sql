package tr2sql;

import net.zemberek.tr.yapi.ek.TurkceEkAdlari;
import net.zemberek.yapi.DilBilgisi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.Kok;
import net.zemberek.yapi.ek.Ek;
import net.zemberek.yapi.ek.EkYonetici;
import org.jmate.FileReader;
import org.jmate.Strings;

import java.io.IOException;
import java.util.*;


/**
 * Kisitli dilbilgisine sahip bir sistemde kelime cozumleme sonrasinda ortaya cikan cozumlerin cogu
 * asil istedigimiz kok ve eklere sahip degildir. Bu nedenle ozellikle cozumleme sonucunda ortaya cikan
 * sonuclarin elenmesi gerekir. Bu sinif eleme islemini yapacak "ele" metodunu kullanir
 * <p/>
 * yaridmci islemler icin http://code.google.com/p/jmate/ kutuphanesinden yararlanilmistir.
 */
public class Tr2SQLKelimeEleyici {

    private DilBilgisi dilBilgisi;

    private Set<Kok> kabulEdilenKokler = new HashSet<Kok>();

    private Set<Ek> kisitlananEkler;

    public Tr2SQLKelimeEleyici(DilBilgisi dilBilgisi) {

        this.dilBilgisi = dilBilgisi;

        try {
            kokleriOku();
        } catch (IOException e) {
            e.printStackTrace();
        }

        EkYonetici ekler = dilBilgisi.ekler();

        //Arrays.asList(Ek...) ile bir ya da daha fazla ek ile bir ArrayList olusturulur.
        List<Ek> kisitliEkListesi = Arrays.asList(
                ekler.ek(TurkceEkAdlari.ISIM_ANDIRMA_IMSI),
                ekler.ek(TurkceEkAdlari.ISIM_ANDIRMA_SI),
                ekler.ek(TurkceEkAdlari.ISIM_TAMLAMA_I),
                ekler.ek(TurkceEkAdlari.ISIM_TAMLAMA_IN),
                ekler.ek(TurkceEkAdlari.ISIM_ILGI_CI),
                ekler.ek(TurkceEkAdlari.ISIM_KISI_ONLAR_LER),
                ekler.ek(TurkceEkAdlari.ISIM_ILISKILI_SEL),
                ekler.ek(TurkceEkAdlari.ISIM_DONUSUM_LE),
                ekler.ek(TurkceEkAdlari.ISIM_KUCULTME_CEGIZ),
                ekler.ek(TurkceEkAdlari.ISIM_KUCULTME_CIK),
                ekler.ek(TurkceEkAdlari.FIIL_SURERLIK_EGOR),
                ekler.ek(TurkceEkAdlari.FIIL_DONUSUM_ESICE),
                ekler.ek(TurkceEkAdlari.FIIL_DONUSUM_ME),
                ekler.ek(TurkceEkAdlari.ISIM_KISI_ONLAR_LER),
                ekler.ek(TurkceEkAdlari.FIIL_SURERLIK_EKAL));

        // bu liste kullanilarak  kisitlananEkler Set'i olusturuluyor.
        kisitlananEkler = new HashSet<Ek>(kisitliEkListesi);

    }

    /**
     * bir ya da daha cok kelimeyi kisitlama kriterlerine gore sinar.
     * sadece kisitlama kriterleirni gecebilenleri bir liste iicnde dondurur.
     *
     * @param kelimeler: kelime dizisi
     * @return kisitlama kriterlerine uyan kelimler. eger hic bir kelime uymazsa bos liste doner.
     */
    public List<Kelime> ele(Kelime... kelimeler) {
        List<Kelime> sonuc = new ArrayList<Kelime>();
        for (Kelime kelime : kelimeler) {
            // eger kelime koku bizim listede yoksa donguye devam et.
            if (!kabulEdilenKokler.contains(kelime.kok()))
                continue;

            // eger eklerden her hangi birisi uygunsuz ise
            boolean kisitliEkBulundu = false;
            for (Ek ek : kelime.ekler()) {
                if (kisitlananEkler.contains(ek)) {
                    kisitliEkBulundu = true;
                    break;
                }
            }
            if (!kisitliEkBulundu)
                sonuc.add(kelime);
        }
        return sonuc;
    }

    private void kokleriOku() throws IOException {

        // bin/temel-kabulEdilenKokler.txt dosyasindan kokleri okuyalim. jmate kutuphanesinin
        // FileReader sinifindan yararlaniliyor.
        // her bir satir listenin bir elemani oluyor.
        List<String> konuOzelStringler =
                new FileReader("bilgi/temel-kokler.txt", "utf-8").asStringList(true);

        // eger satir bos ise ya da # karakteri ile basliyorsa bunlari dikkate ala.
        konuOzelStringler = gecersizSatrilariEle(konuOzelStringler);
        SozlukIslemleri sozlukIslemleri = new SozlukIslemleri(dilBilgisi.kokler());

        for (String s : konuOzelStringler) {
            // elimizden geldigince kok Stringininn yazilisina gore gercekte hangi koke karsilik dustugunu buluyoruz.
            Kok k = sozlukIslemleri.kokTahminEt(s);
            if (k != null)
                this.kabulEdilenKokler.add(k);
            else {
                // kok bulunamadigindan zemberege yeni kok olarak ekliyoruz.  
                Kok kok = sozlukIslemleri.tahminEtVeEkle(s);
                kabulEdilenKokler.add(kok);
                //System.out.println(s + " icin kok bulunamadi. yeni kok eklenecek:" + kok);
            }
        }
    }


    private List<String> gecersizSatrilariEle(List<String> list) {
        List<String> yeni = new ArrayList<String>();
        for (String s : list) {
            if (Strings.hasText(s) && !s.startsWith("#")) {
                yeni.add(s);
            }
        }
        return yeni;
    }

}
