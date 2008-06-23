package tr2sql.db;

import net.zemberek.yapi.KelimeTipi;
import net.zemberek.yapi.Kok;

import java.util.Arrays;
import java.util.List;

import tr2sql.cozumleyici.BilgiBileseni;

/**
 * Microsoft SQL Server icin Sorgu degerlerinden SQL cumlesi uretici.
 */
public class MsSqlDonusturucu implements SqlDonusturucu {

    public String donustur(SorguTasiyici sorgu) {

        StringBuilder sonuc = new StringBuilder();

        if (sorgu.islemTipi == null) {
            sorgu.rapor.append("Islem belirten kelime bulunamadi, sorgulama yapildigi var sayilacak. ");
            sorgu.islemTipi = IslemTipi.SORGULAMA;
        }
        sonuc.append(sorgu.islemTipi.sqlDonusumu());


        if (sorgu.saymaSorgusu) {
            sonuc.append(" count (*) ");
        } else {        
        // eger belirtilmisse sonuc miktar kisitlama bilgisini ekleyelim.
        if (sorgu.sonucMiktarKisitlamaDegeri > -1)
            sonuc.append(" top ").append(sorgu.sonucMiktarKisitlamaDegeri).append(" ");

        // sadece belirtilen kolonlarin donmesi istenmisse bunlari ekleyelim
        int i = 0;
        for (Kolon kolon : sorgu.sonucKolonlari) {
            sonuc.append(kolon.getAd());
            if (i++ < sorgu.sonucKolonlari.size() - 1)
                sonuc.append(", ");
        }
        // eger donus kolonlari belirtilmemisse herseyi dondurelim.
        if (sorgu.sonucKolonlari.isEmpty())
            sonuc.append(" * ");
        }
        // tabloyu ekleyelim
        if (sorgu.tablo == null)
            throw new SQLUretimHatasi("Tablo bulunamadi..");
        sonuc.append(" from ").append(sorgu.tablo.getAd()).append(" ");

        // kisitlama zincir bileseni varsa "where" kelimesini ekleyelim.
        if (!sorgu.kolonKisitlamalari.isEmpty())
            sonuc.append(" where ");

        //eger kisitlama bileseni mevcutsa ekleyelim.
        for (KolonKisitlamaBileseni bilesen : sorgu.kolonKisitlamalari) {
            sonuc.append(bilesen.sqlDonusumu());
        }

        if (sorgu.rapor.length() > 0) {
            sonuc.append("\n\n-------------------------------\n");
            sonuc.append(sorgu.rapor);
        }

        // sonuctaki birden fazla bosluklari bir bosluga indir ve sondaki boslugu kirp.
        return sonuc.toString().replaceAll("[ ]+", " ").trim();
    }

    public static void main(String[] args) {

        // sorgu verisini tasiyan nesnenin ilk olusumu.
        SorguTasiyici sorgu = new SorguTasiyici();

        // tip belirle.
        sorgu.islemTipi = IslemTipi.SORGULAMA;

        // tablo ve kolon uretiminde gerekli oldugundan ornek bir kavramdanTipBul uretelim. yoksa SQL uretiminde gerekli degil.
        Kavram testKavrami = new Kavram("testkavrami", Arrays.asList(new Kok("testkok", KelimeTipi.ISIM)));

        // iki test kolonu
        Kolon kolon1 = new Kolon("NUMARA", testKavrami, KolonTipi.SAYI, true);
        Kolon kolon2 = new Kolon("ISIM", testKavrami, KolonTipi.YAZI, false);
        List<Kolon> kolonlar = Arrays.asList(kolon1, kolon2);

        // test tablosu.
        sorgu.tablo = new Tablo("TEST_TABLOSU", testKavrami, kolonlar);

        // farzedelin sql sorgu sonucu 10 satir ile kisitlanmak istensin.
        sorgu.sonucMiktarKisitlamaDegeri = 10;

        // iki tane kolon kisitlama verimiz olsun. ilki "numarasi [5]'ten buyuk" kelimelerinden turemis olsun.
        BilgiBileseni bb = new BilgiBileseni("5", KiyasTipi.BUYUK);
        KolonKisitlamaBileseni numaraKisitlama = new KolonKisitlamaBileseni(kolon1, bb, BaglacTipi.YOK);
        // ikinci kisitlama verisi ise "adi [Ali] olan" kelimelerinden turemis olsun..
        bb = new BilgiBileseni("Ali", KiyasTipi.ESIT);
        KolonKisitlamaBileseni isimKisitlama = new KolonKisitlamaBileseni(kolon2, bb, BaglacTipi.VE);

        sorgu.kolonKisitlamalari = Arrays.asList(numaraKisitlama, isimKisitlama);

        SqlDonusturucu sqlDonusturucu = new MsSqlDonusturucu();

        System.out.println("sonuc:" + sqlDonusturucu.donustur(sorgu));

    }

}
