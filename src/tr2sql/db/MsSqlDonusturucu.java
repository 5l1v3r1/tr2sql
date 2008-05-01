package tr2sql.db;

import net.zemberek.yapi.Kok;
import net.zemberek.yapi.KelimeTipi;

import java.util.List;

import org.jmate.collections.Lists;

/**
 * Microsoft SQL Server icin Sorgu degerlerinden SQL cumlesi uretici.
 */
public class MsSqlDonusturucu implements SqlDonusturucu {

    public String donustur(SorguTasiyici sorgu) {

        StringBuilder sonuc = new StringBuilder();

        switch (sorgu.islemTipi) {
            case SORGULAMA:
                sonuc.append("select ");
                break;
            case GUNCELLEME:
                sonuc.append("update "); // bunu kullanmayacagiz ama ornek olsun.
                break;
        }

        // eger belirtilmisse sonuc miktar kisitlama bilgisini ekleyelim.
        if (sorgu.sonucMiktarKisitlamaDegeri > -1)
            sonuc.append(" top ").append(sorgu.sonucMiktarKisitlamaDegeri).append(" ");

        // sadece belirtilen kolonlarin donmesi istenmisse bunlari ekleyelim
        int i = 0;
        for (Kolon kolon : sorgu.sonucKolonlari) {
            sonuc.append(kolon.getAd());
            if (i++ < sorgu.sonucKolonlari.size())
                sonuc.append(",");
        }

        // eger donus kolonlari belirtilmemisse herseyi dondurelim.
        if (sorgu.sonucKolonlari.isEmpty())
            sonuc.append(" * ");

        // tabloyu ekleyelim
        sonuc.append(" from ").append(sorgu.tablo.getAd()).append(" ");

        // kisitlama zincir bileseni varsa "where" kelimesini ekleyelim.
        if (!sorgu.kolonKisitlamaZinciri.isEmpty())
            sonuc.append(" where ");

        //eger kisitlama bileseni mevcutsa ekleyelim.
        for (KolonKisitlamaZincirBileseni bilesen : sorgu.kolonKisitlamaZinciri) {
            sonuc.append(bilesen.sqlDonusumu());
        }

        // sonuctaki birden fazla bosluklari bir bosluga indir ve sondaki boslugu kirp.
        return sonuc.toString().replaceAll("[ ]+", " ").trim();
    }

    public static void main(String[] args) {

        // sorgu verisini tasiyan nesnenin ilk olusumu.
        SorguTasiyici sorgu = new SorguTasiyici();

        // tip belirle.
        sorgu.islemTipi = IslemTipi.SORGULAMA;

        // tablo ve kolon uretiminde gerekli oldugundan ornek bir kavram uretelim. yoksa SQL uretiminde gerekli degil.
        Kavram testKavrami = new Kavram("testkavrami", Lists.newArrayList(new Kok("testkok", KelimeTipi.ISIM)));

        // iki test kolonu
        Kolon kolon1 = new Kolon("NUMARA", testKavrami, KolonTipi.SAYI, true);
        Kolon kolon2 = new Kolon("ISIM", testKavrami, KolonTipi.YAZI, false);
        List<Kolon> kolonlar = Lists.newArrayList(kolon1, kolon2);

        // test tablosu.
        sorgu.tablo = new Tablo("TEST_TABLOSU", testKavrami, kolonlar);

        // farzedelin sql sorgu sonucu 10 satir ile kisitlanmak istensin.
        sorgu.sonucMiktarKisitlamaDegeri = 10;

        // iki tane kolon kisitlama verimiz olsun. ilki "numarasi [5]'ten buyuk" kelimelerinden turemis olsun.
        KolonKisitlamaBileseni numaraKisitlama = new KolonKisitlamaBileseni(kolon1, "5", KiyasTipi.BUYUK);
        // ikinci kisitlama verisi ise "adi [Ali] olan" kelimelerinden turemis olsun..
        KolonKisitlamaBileseni isimKisitlama = new KolonKisitlamaBileseni(kolon2, "Ali", KiyasTipi.ESIT);

        // kisitlama bilgilerini "ve" ile birbirine bagla.
        KolonKisitlamaZincirBileseni halka1 = new KolonKisitlamaZincirBileseni(numaraKisitlama, KosulTipi.VE);
        KolonKisitlamaZincirBileseni halka2 = new KolonKisitlamaZincirBileseni(isimKisitlama, KosulTipi.YOK);

        //bilesenleri bir Linkli liste seklinde sorgu tasiyiciya koyalim
        sorgu.kolonKisitlamaZinciri = Lists.newLinkedList(halka1, halka2);

        SqlDonusturucu sqlDonusturucu = new MsSqlDonusturucu();

        // TODO: Asagidaki islemin "select top 10 * from TEST_TABLOSU where NUMARA>5 and ISIM='Ali'" uretmesi gerekir..

        System.out.println("sonuc:" + sqlDonusturucu.donustur(sorgu));

    }

}
