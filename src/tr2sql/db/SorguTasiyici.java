package tr2sql.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Sorgu Tasiyici, Dogal dil cumleden elde edilen sorgu bilesenlerini tasiyan bir yapidir.
 */
public class SorguTasiyici {

    // SQL islen tipi
    public IslemTipi islemTipi;

    // ilgili tablo.
    public Tablo tablo;

    // sorguyu kisitlayan sutun bilgileri
    public List<SutunKisitlamaBileseni> sutunKisitlamalari = new ArrayList<SutunKisitlamaBileseni>();

    // sonuc miktari. -1 ise miktar kisitlamasi yok demektir.
    public int sonucMiktarKisitlamaDegeri = -1;

    // sonucta listelenmesi gereken sutunlar.
    public List<Sutun> sonucSutunlari = new ArrayList<Sutun>();

    public boolean saymaSorgusu = false;

    public StringBuilder rapor = new StringBuilder();

    public SorguTasiyici raporla(String s) {
        rapor.append(s).append("\n");
        return this;
    }

}
