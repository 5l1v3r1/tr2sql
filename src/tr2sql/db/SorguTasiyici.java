package tr2sql.db;

import org.jmate.collections.Lists;

import java.util.List;


public class SorguTasiyici {

    public IslemTipi islemTipi;
    public Tablo tablo;
    public List<KolonKisitlamaZincirBileseni> kolonKisitlamaZinciri = Lists.newLinkedList();
    // -1 ise miktar kisitlamasi yok demektir.
    public int sonucMiktarKisitlamaDegeri = -1;
    public List<Kolon> sonucKolonlari = Lists.newArrayList();

}
