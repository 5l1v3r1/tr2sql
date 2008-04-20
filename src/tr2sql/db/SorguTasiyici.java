package tr2sql.db;

import org.jmate.collections.Lists;

import java.util.List;

/**
 * Sorgu Tasiyici, Dogal dil cumleden elde edilen sorgu bilesenlerini tasiyan bir tasitici yapidir.
 */
public class SorguTasiyici {

    // SQL islen tipi
    public IslemTipi islemTipi;

    // ilgili tablo.
    public Tablo tablo;

    // sorguyu kisitlayan kolon bilgileri
    public List<KolonKisitlamaZincirBileseni> kolonKisitlamaZinciri = Lists.newLinkedList();

    // sonuc miktari. -1 ise miktar kisitlamasi yok demektir.
    public int sonucMiktarKisitlamaDegeri = -1;

    // sonucta listelenmesi gereken kolonlar.
    public List<Kolon> sonucKolonlari = Lists.newArrayList();
}
