package tr2sql.gui;

import net.zemberek.araclar.turkce.YaziBirimi;
import net.zemberek.araclar.turkce.YaziBirimiTipi;
import net.zemberek.araclar.turkce.YaziIsleyici;
import net.zemberek.erisim.Zemberek;
import net.zemberek.yapi.DilBilgisi;
import net.zemberek.yapi.Kelime;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;

import java.util.*;
import java.io.IOException;

import tr2sql.Tr2SQLKelimeEleyici;
import tr2sql.db.TurkceSQLCozumleyici;
import tr2sql.db.Tablo;

/**
 */
public class DemoYonetici {


    private Zemberek zemberek = new Zemberek(new TurkiyeTurkcesi());
    private DilBilgisi dilBilgisi = zemberek.dilBilgisi();
    private Tr2SQLKelimeEleyici eleyici = new Tr2SQLKelimeEleyici(dilBilgisi);
    private TurkceSQLCozumleyici tr2SQLCozumleyici;


    public DemoYonetici() {
        try {
            tr2SQLCozumleyici = new TurkceSQLCozumleyici(
                    zemberek,
                    "bilgi/basit-veri-tabani.xml",
                    "bilgi/kavramlar.txt");
        } catch (IOException e) {
            System.err.println("Muhtemelen dosya okuma sirasinda bir hata oldu..");
            e.printStackTrace();
        }
    }

    /**
     * turkceyeozel karakterlerin dizisini dondurur
     *
     * @return char dizisi. turkceye ozel karakterler yer alir.
     */
    public char[] ozelKarakterDizisiGetir() {
        return dilBilgisi.alfabe().asciiDisiHarfler();
    }

    public String kisitliCozumle(String giris) {
        List<YaziBirimi> analizDizisi = YaziIsleyici.analizDizisiOlustur(giris);
        StringBuffer sonuc = new StringBuffer();
        for (YaziBirimi birim : analizDizisi) {
            if (birim.tip == YaziBirimiTipi.KELIME) {
                List<Kelime> cozumler = eleyici.ele(zemberek.kelimeCozumle(birim.icerik));
                sonuc.append(birim.icerik).append('\n');
                if (cozumler.size() == 0)
                    sonuc.append(" :cozulemedi\n");
                else {
                    for (Kelime cozum : cozumler)
                        sonuc.append(cozum).append("\n");
                }
            }
        }
        return sonuc.toString();
    }

    public String yaziCozumle(String giris) {
        List<YaziBirimi> analizDizisi = YaziIsleyici.analizDizisiOlustur(giris);
        StringBuffer sonuc = new StringBuffer();
        for (YaziBirimi birim : analizDizisi) {
            if (birim.tip == YaziBirimiTipi.KELIME) {
                Kelime[] cozumler = zemberek.kelimeCozumle(birim.icerik);
                sonuc.append(birim.icerik).append('\n');
                if (cozumler.length == 0)
                    sonuc.append(" :cozulemedi\n");
                else {
                    for (Kelime cozum : cozumler)
                        sonuc.append(cozum).append("\n");
                }
            }
        }
        return sonuc.toString();
    }

    public String veriTabaniBilgileriniYaz() {
        return tr2SQLCozumleyici.getVeriTabani().toString();
    }

    public String tabloVeKolonTahminGoster(String giris) {
        //Tablo tablo = tr2SQLCozumleyici.tabloTahminEt(giris);
        return "Henuz tamamlanmadi.....";
    }
}
