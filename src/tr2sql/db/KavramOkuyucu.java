package tr2sql.db;

import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.DilBilgisi;
import net.zemberek.yapi.Kok;
import net.zemberek.yapi.TurkceDilBilgisi;
import org.jmate.FileReader;
import org.jmate.Strings;
import tr2sql.SozlukIslemleri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author dilek
 */
public class KavramOkuyucu {

    private SozlukIslemleri sozlukIslemleri;

    public KavramOkuyucu(SozlukIslemleri sozlukIslemleri) {
        this.sozlukIslemleri = sozlukIslemleri;
    }

    public List<Kavram> oku(String KavramDosyaAdi) throws IOException {
        List<Kavram> kavramlar = new ArrayList<Kavram>();
        List<String> kavramKarsiliklari = new FileReader(KavramDosyaAdi, "utf-8").asStringList();
        for (String s : kavramKarsiliklari) {
            //bos satirlari islemeden atla..
            if (!Strings.hasText(s))
                continue;

            String[] dizi = s.trim().split(",|\n|:");

            if (dizi.length < 2) {
                System.out.println("Bir kavram icin en az bir kok karsiligi yazilmali:" + s);
                continue;
            }

            String kavramKelimesi = dizi[0].trim();
            List<Kok> kavramKokleri = new ArrayList<Kok>();

            for (int i = 1; i < dizi.length; i++) {
                String kokStr = dizi[i].trim();
                Kok kok = sozlukIslemleri.kokTahminEt(kokStr);
                if (kok == null) {
                    kok = sozlukIslemleri.tahminEtVeEkle(kokStr);
                    System.out.println(kokStr + " koku Zemberek icinde bulunamadi ve yeni kok olarak eklendi:" + kok.toString());
                }
                kavramKokleri.add(kok);
            }
            kavramlar.add(new Kavram(kavramKelimesi, kavramKokleri));
        }
        return kavramlar;
    }

    public static void main(String[] args) throws IOException {
        DilBilgisi db = new TurkceDilBilgisi(new TurkiyeTurkcesi());
        KavramOkuyucu ko = new KavramOkuyucu(new SozlukIslemleri(db.kokler()));
        List<Kavram> kavramlar = ko.oku("bilgi/kavramlar.txt");
        System.out.println("kavramlar = " + kavramlar);
    }
}
    
    
    
    
      
    

