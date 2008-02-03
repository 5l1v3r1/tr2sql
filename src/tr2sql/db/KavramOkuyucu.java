/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr2sql.db;
import net.zemberek.tr.yapi.ek.TurkceEkAdlari;
import net.zemberek.yapi.DilBilgisi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.KelimeTipi;
import net.zemberek.yapi.Kok;
import net.zemberek.yapi.ek.Ek;
import net.zemberek.yapi.ek.EkYonetici;

import java.util.*;
import java.io.IOException;

import org.jmate.Files;
import org.jmate.Strings;
import org.jmate.collections.Lists;
/**
 *
 * @author dilek
 */
public class KavramOkuyucu {
    
    private Set<Kavram> kavramlar= new HashSet <Kavram>();
           
    public List<String> oku (String KavramDosyaAdi) throws IOException
    {
        List<String> kavramKarsiliklari=Files.readAsStringList(KavramDosyaAdi,"utf-8",true);
        kavramKarsiliklari=getir(kavramKarsiliklari); 
        return kavramKarsiliklari;        
    }
         
    private  List <String> getir(List<String> list)
    {
        List <String> yeni=Lists.newArrayList(); 
        String [] dizi=null;
        for(String s : list)
        {
            if(Strings.hasText(s))
            {
                //split ile kavram.txt ten okutulan stringleri ayırmaya çalışıyor. bir diziye atıyor.
                //direk listeye atasın diye çok istedim fakat hata verdi.
              dizi= s.split(",|\n|:");
            }
        }
        yeni=dizidenListeye(dizi);
        return yeni;        
    }
    //burada da diziden listeye atıyor. string listesi halinde döndürüyor.
    private  List <String> dizidenListeye(String [] s)
    {      
        List <String> y=Lists.newArrayList();       
        for(int i=0;i<s.length;i++)
        {
           y.add(s[i]);
        }
        return y;
    }                      
}
    
    
    
    
      
    

