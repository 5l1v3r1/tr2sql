package tr2sql.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Sorgu Tasiyici, Dogal dil cumleden elde edilen sorgu bilesenlerini tasiyan bir yapidir.
 */
public class SorguTasiyici {

    // SQL islen tipi
    public IslemTipi islemTipi;

    // ilgili tablo.
    public Tablo tablo;

    // sorguyu kisitlayan kolon bilgileri
    public List<KolonKisitlamaBileseni> kolonKisitlamalari =  new ArrayList<KolonKisitlamaBileseni>();

    // sonuc miktari. -1 ise miktar kisitlamasi yok demektir.
    public int sonucMiktarKisitlamaDegeri = -1;

    // sonucta listelenmesi gereken kolonlar.
    public List<Kolon> sonucKolonlari = new ArrayList<Kolon>();

    public boolean saymaSorgusu = false;    

    public StringBuilder rapor = new StringBuilder();

    public SorguTasiyici raporla(String s) {
        rapor.append(s).append("\n");
        return this;
    }

}
