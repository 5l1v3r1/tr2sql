package tr2sql.db;

import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.tr.yapi.ek.TurkceEkAdlari;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.Kok;
import tr2sql.SozlukIslemleri;
import tr2sql.Tr2SQLKelimeEleyici;
import tr2sql.cozumleyici.*;
import tr2sql.cozumleyici.BasitDurumMakinesi;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TurkceSQLCozumleyici {

    private VeriTabani veriTabani;
    private Map<String, Kavram> stringKavramTablosu = new HashMap<String, Kavram>();
    private Map<Kok, Kavram> kokKavramTablosu = new HashMap<Kok, Kavram>();

    private Zemberek zemberek;
    private Tr2SQLKelimeEleyici eleyici;

    public TurkceSQLCozumleyici(Zemberek zemberek,
                                Tr2SQLKelimeEleyici eleyici,
                                String veriTabaniDosyasi,
                                String kavramDosyasi) throws IOException {
        this.zemberek = zemberek;
        this.eleyici = eleyici;
        SozlukIslemleri sozlukIslemleri = new SozlukIslemleri(zemberek.dilBilgisi().kokler());
        KavramOkuyucu kavramOkuyucu = new KavramOkuyucu(sozlukIslemleri);

        // kavramlari okuyup tablolara at.
        List<Kavram> kavramlar = kavramOkuyucu.oku(kavramDosyasi);
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

    public List<CumleBileseni> sorguCumleBilesenleriniAyir(String giris) {
        return new BasitCumleCozumleyici(giris).bilesenler();
    }

    public List<String> kelimeAyristir(String giris) {
        return new BasitCumleCozumleyici(giris).cumleParcalari;
    }

    public String sqlDonusum(String giris) {
        SorguTasiyici st = new BasitDurumMakinesi(sorguCumleBilesenleriniAyir(giris)).islet();
        return new MsSqlDonusturucu().donustur(st);
    }

    class BasitCumleCozumleyici {

        private List<String> cumleParcalari = new ArrayList<String>();

        public BasitCumleCozumleyici(String giris) {

            // birden fazla bosluklari tek bosluga indir.
            String c = giris.replaceAll("[ ]+", " ").trim();

            // "ya da" ikilisini "veya" ile degistirelim. coklu kelimelerle ugrasmamak icin.
            c = c.replaceAll("ya da", "veya");

            // bu regular expression ile cumledeki kelimeleri parcaliyoruz.
            // eger kelime '' isareti icinde ise parcalanmiyor, butun olarak aliniyor.
            // virgul sembolu de ayrica listede yer aliyor. mesela
            // "adi 'ayse','ali can' olan ogrencileri goster." cumlesinden
            // [adi]['ayse'][,]['ali can'][olan][ogrencileri][goster]
            // parcalari elde edilir..
            Pattern parcalayici = Pattern.compile("(\"[^\"]*\")|[^ \\t\\n,.]+|,");
            Matcher m = parcalayici.matcher(c);

            while (m.find())
                cumleParcalari.add(m.group());
        }

        private List<CumleBileseni> bilesenler() {
            List<CumleBileseni> bilesenler = new ArrayList<CumleBileseni>();

            BaglacTipi baglacTipi = BaglacTipi.YOK;

            for (String s : cumleParcalari) {

                // virgul, ve, veya gibi baglaclari hatirlayip kendinden sonra gelen Kolon ya da
                // Bilgi Bilesenlerine ekliyoruz.
                BaglacTipi t = BaglacTipi.baglacTipiTahminEt(s);
                if (t != BaglacTipi.YOK) {
                    baglacTipi = t;
                    continue;
                }

                // sayi mi? henuz sadece rakam ile yazilmissa buluyor.
                try {
                    int sayi = Integer.parseInt(s);
                    bilesenler.add(new SayiBileseni(sayi));
                    baglacTipi = BaglacTipi.YOK;
                    continue;
                } catch (NumberFormatException e) {
                    //bir sey yapma..
                }

                // bilgi bileseni. "" isareti icinde olur.
                if (s.startsWith("\"") && s.length() > 2) {
                    BilgiBileseni bilesen = new BilgiBileseni(s.substring(1, s.length() - 1));
                    bilesen.setOnBaglac(baglacTipi);
                    bilesenler.add(bilesen);
                    baglacTipi = BaglacTipi.YOK;
                    continue;
                }

                // diger bilesenleri ortaya cikarmak icin kelime cozumlenip kokunden hangi kavrama
                // karsilik dustugu belirleniyor.
                List<Kelime> sonuclar = eleyici.ele(zemberek.kelimeCozumle(s, CozumlemeSeviyesi.TUM_KOK_VE_EKLER));
                Kavram kavram;
                Kelime kelime;
                if (sonuclar.size() > 0) {
                    kelime = sonuclar.get(0);
                    kavram = kokKavramTablosu.get(kelime.kok());
                } else {
                    bilesenler.add(new TanimsizBilesen(s));
                    baglacTipi = BaglacTipi.YOK;
                    continue;
                }
                CumleBileseni bilesen = bilesenBul(kavram, s, kelime);

                if (bilesen.tip() == CumleBilesenTipi.KOLON)
                    ((KolonBileseni) bilesen).setOnBaglac(baglacTipi);

                bilesenler.add(bilesen);
                baglacTipi = BaglacTipi.YOK;
            }
            return bilesenler;
        }

        private boolean olumsuzlukEkiVarmi(Kelime kel) {
            return kel.ekler().contains(
                    zemberek.dilBilgisi().ekler().ek(TurkceEkAdlari.FIIL_OLUMSUZLUK_ME));
        }

        // kavrama gore sorgu cumle bilesenini bulur.
        private CumleBileseni bilesenBul(Kavram kavram, String s, Kelime kelime) {

            if (kavram == null)
                return new TanimsizBilesen(s);

            String kavramAdi = kavram.getAd();
            boolean olumsuzlukEkiVar = olumsuzlukEkiVarmi(kelime);

            if (kavramAdi.equals("ILK")) {
                return new MiktarKisitlamaBileseni(s);
            }

            // tablo bilesni mi?
            Tablo t = veriTabani.kavramaGoreTabloBul(kavram);
            if (t != null)
                return new TabloBileseni(t, kelime);

            // islem bileseni mi? goster, listele vs.
            IslemTipi tip = IslemTipi.kavramaGoreIslem(kavram);
            if (tip != IslemTipi.TANIMSIZ) {
                IslemBileseni b = new IslemBileseni(tip, kelime);
                b.setOlumsuzluk(olumsuzlukEkiVar);
                return b;
            }

            // kolon bileseni mi?
            List<Kolon> tumKolonlar = veriTabani.tumKolonlar();
            for (Kolon kolon : tumKolonlar) {
                if (kolon.getKavram().equals(kavram))
                    return new KolonBileseni(kolon, kelime);
            }

            // kiyaslama bileseni mi? buyuk, kucuk, esit vs.
            KiyasTipi kiyasTipi = KiyasTipi.kavramdanTipBul(kavram);

            if (kiyasTipi != null) {
                return new KiyaslamaBileseni(kiyasTipi, kelime, olumsuzlukEkiVar);
            }

            if (kavram.getAd().equals("OLMAK")) {
                return new OlmakBIleseni(kelime, olumsuzlukEkiVar);
            }

            return new TanimsizBilesen(s);
        }
    }
}
