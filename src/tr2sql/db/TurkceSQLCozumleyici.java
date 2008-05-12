package tr2sql.db;

import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.Kok;
import tr2sql.SozlukIslemleri;
import tr2sql.cozumleyici.*;

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

    public List<SorguCumleBileseni> sorguCumleBilesenleriniAyir(String giris) {
        return new BasitCumleCozumleyici(giris).bilesenler();
    }

    class BasitCumleCozumleyici {

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
        }

        private List<SorguCumleBileseni> bilesenler() {
            List<SorguCumleBileseni> bilesenler = new ArrayList<SorguCumleBileseni>();

            for (String s : cumleParcalari) {

                if (s.startsWith("'") && s.length() > 2) {
                    SorguCumleBileseni bilesen = new KisitlamaBileseni(s.substring(1, s.length() - 1));
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
            return bilesenler;
        }

        // kavrama gore sorgu cumle bilesenini bulur.
        private SorguCumleBileseni bilesenBul(Kavram kavram, String s) {

            if (kavram == null)
                return new TanimsizBilesen(s);

            Tablo t = veriTabani.kavramaGoreTabloBul(kavram);
            if (t != null)
                return new TabloBileseni(t, s);

            IslemTipi tip = IslemTipi.kavramaGoreIslem(kavram);
            if (tip != IslemTipi.TANIMSIZ)
                return new IslemBileseni(tip, s);

            List<Kolon> tumKolonlar = veriTabani.tumKolonlar();
            for (Kolon kolon : tumKolonlar) {
                if (kolon.getKavram().equals(kavram))
                    return new KolonBileseni(kolon, s);
            }

            if (kavram.getAd().equals("ol"))
                return new TemelKavramBileseni(CumleBilesenTipi.KISITLAMA_TANIMLAYICI, s);

            return new TanimsizBilesen(s);
        }

    }
}
