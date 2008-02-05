/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr2sql.db;

import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.*;

import java.util.*;
import java.io.IOException;

import org.jmate.Files;
import org.jmate.Strings;
import org.jmate.collections.Lists;
import org.jmate.collections.Sets;
import tr2sql.SozlukIslemleri;

/**
 * @author dilek
 */
public class KavramOkuyucu {

    private SozlukIslemleri sozlukIslemleri;

    public KavramOkuyucu(SozlukIslemleri sozlukIslemleri) {
        this.sozlukIslemleri = sozlukIslemleri;
    }

    public Set<Kavram> oku(String KavramDosyaAdi) throws IOException {
        Set<Kavram> kavramlar = Sets.newHashSet();
        List<String> kavramKarsiliklari = Files.readAsStringList(KavramDosyaAdi, "utf-8", true);
        for (String s : kavramKarsiliklari) {
            //bos satirlari islemeden atla..
            if (!Strings.hasText(s))
                continue;

            String[] dizi = s.split(",|\n|:");

            if (dizi.length < 2) {
                System.out.println("Bir kavram icin en az bir kok karsiligi yazilmali:" + s);
                continue;
            }

            String kavramKelimesi = dizi[0].trim();
            List<Kok> kavramKokleri = Lists.newArrayList();

            for (int i = 1; i < dizi.length; i++) {
                String kokStr = dizi[i].trim();
                Kok kok = sozlukIslemleri.kokTahminEt(kokStr);
                if (kok == null) {
                    kok = sozlukIslemleri.tahminEtVeEkle(kokStr);
                    System.out.println(kokStr + " koku Zemberek icinde bulunamadi ve yeni kok olarak eklendi:" + kok.toString());
                }
                kavramKokleri.add(kok);
            }
            Kavram kavram = new Kavram(kavramKelimesi, kavramKokleri);
            kavramlar.add(kavram);
        }
        return kavramlar;
    }

    public static void main(String[] args) throws IOException {
        DilBilgisi db = new TurkceDilBilgisi(new TurkiyeTurkcesi());
        KavramOkuyucu ko = new KavramOkuyucu(new SozlukIslemleri(db.kokler()));
        Set<Kavram> kavramlar = ko.oku("bilgi/kavramlar.txt");
        System.out.println("kavramlar = " + kavramlar);
    }
}
    
    
    
    
      
    

