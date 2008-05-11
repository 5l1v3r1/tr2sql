package tr2sql.db;

import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.Kok;
import tr2sql.SozlukIslemleri;
import tr2sql.cozumleyici.KisitlamaBileseni;
import tr2sql.cozumleyici.SorguCumleBileseni;
import tr2sql.cozumleyici.TanimsizBilesen;
import tr2sql.cozumleyici.TabloBileseni;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class TurkceSQLCozumleyici {

    private VeriTabani veriTabani;
    private Map<String, Kavram> stringKavramTablosu = new HashMap<String, Kavram>();
    private Map<Kok, Kavram> kokKavramTablosu = new HashMap<Kok, Kavram>();

    private Zemberek zemberek;

    public TurkceSQLCozumleyici(Zemberek zemberek,
                                String veriTabaniDosyasi,
                                String kavramDosyasi) throws IOException {
        this.zemberek = zemberek;
        SozlukIslemleri sozlukIslemleri = new SozlukIslemleri(zemberek.dilBilgisi().kokler());
        KavramOkuyucu kavramOkuyucu = new KavramOkuyucu(sozlukIslemleri);

        // kavramlari okuyup tablolara at.
        Set<Kavram> kavramlar = kavramOkuyucu.oku(kavramDosyasi);
        for (Kavram kavram : kavramlar) {
            stringKavramTablosu.put(kavram.getAd(), kavram);
            for (Kok kok : kavram.getEsKokler()) {
                kokKavramTablosu.put(kok, kavram);
            }
        }
        veriTabani = new XmlVeriTabaniBilgisiOkuyucu(stringKavramTablosu).oku(veriTabaniDosyasi);
    }

    public VeriTabani getVeriTabani() {
        return veriTabani;
    }

    public Map<String, Kavram> getStringKavramTablosu() {
        return stringKavramTablosu;
    }

    public Tablo tabloTahminEt(String giris) {
        return new BasitCumleCozumleyici(giris).tabloTahminEt();
    }


    public IslemTipi islemTipiTahminEt(String giris) {
        return new BasitCumleCozumleyici(giris).islemBul();
    }

    class BasitCumleCozumleyici {

        private List<Kelime> olasiKelimeDizisi = new ArrayList<Kelime>();
        private List<String> cumleParcalari = new ArrayList<String>();

        public BasitCumleCozumleyici(String giris) {

            String c = giris.replaceAll("[ ]+", " ").trim();

            // bu regular expression ile cumledeki kelimeleri parcaliyoruz.
            // eger kelime '' isareti icinde ise parcalanmiyor, butun olarak aliniyor.
            // virgul sembolu de ayrica listede yer aliyor. mesela
            // "adi 'ayse','ali can' olan ogrencileri goster." cumlesinden
            // [adi]['ayse'][,]['ali can'][olan][ogrencileri][goster]
            // parcalari elde edilir..
            Pattern parcalayici = Pattern.compile("('[^']*')|[^ \\t\\n,.]+|,");
            Matcher m = parcalayici.matcher(c);

            while (m.find())
                cumleParcalari.add(m.group());


            List<SorguCumleBileseni> bilesenler = new ArrayList<SorguCumleBileseni>();

            for (String s : cumleParcalari) {

                if (s.startsWith("'") && s.length() > 2) {
                    SorguCumleBileseni bilesen = new KisitlamaBileseni(s.substring(1, s.length() - 2));
                    bilesenler.add(bilesen);
                } else {
                    Kelime[] sonuclar = zemberek.kelimeCozumle(s, CozumlemeSeviyesi.TUM_KOKLER);
                    Kavram kavram;
                    if (sonuclar.length > 0)
                        kavram = kokKavramTablosu.get(sonuclar[0].kok());
                    else {
                        bilesenler.add(new TanimsizBilesen(s));
                        continue;
                    }
                    bilesenler.add(bilesenBul(kavram, s));
                }
            }
        }

        // kavrama gore sorgu cumle bilesenini bulur.
        private SorguCumleBileseni bilesenBul(Kavram kavram, String s) {
            Tablo t = veriTabani.kavramaGoreTabloBul(kavram);
            if (t != null)
                return new TabloBileseni();
            return new TanimsizBilesen(s);

        }

        /**
         * simdilik tek tablo buluyor.
         *
         * @return bulunursa Tablo. yoksa Exception firlatir..
         */
        public Tablo tabloTahminEt() {

            for (Kelime kelime : olasiKelimeDizisi) {
                Tablo t = veriTabani.kokeGoreTabloBul(kelime.kok());
                if (t != null)
                    return t;
            }
            return null;
        }

        /**
         * ilgili islemi bulur. simdilik sadece sorgulama kavrami ile calisiyor.
         * ve soru cumleleri ihmal ediliyor.
         *
         * @return bulunan islem tipi.
         */
        public IslemTipi islemBul() {
            Kavram sorguKavrami = stringKavramTablosu.get("sorgu");
            for (Kelime kelime : olasiKelimeDizisi) {
                if (sorguKavrami.kokMevcutMu(kelime.kok()))
                    return IslemTipi.SORGULAMA;
            }
            return IslemTipi.SORGULAMA;
        }

        public List<Kelime> getOlasiKelimeDizisi() {
            return olasiKelimeDizisi;
        }

        public List<KolonKisitlamaBileseni> kisitlamaBilesenListesi(Tablo tablo) {

            List<KolonKisitlamaBileseni> bilesenler = new ArrayList<KolonKisitlamaBileseni>();
            Set<Integer> islenenKolonlar = new HashSet<Integer>();
            for (Kelime kelime : olasiKelimeDizisi) {
                // once bu kelimenin koku bir kolona denk dusuyormu bakalim.
                Kolon kolon = tablo.kokeGoreKolonBul(kelime.kok());
                // denk dusmuyorsa donguye devam et.
                if (kolon == null)
                    continue;
                //burasi biraz dandik. kolon son eki "i" belirtme ya da "li" eki ise
                // farkli seklide deger aramamiz gerekecek..
                // TODO: devami gelecek.


            }
            return bilesenler;
        }

    }

}
