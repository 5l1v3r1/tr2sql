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

            // sadece belirtilen sutunlarin donmesi istenmisse bunlari ekleyelim
            int i = 0;
            for (Sutun sutun : sorgu.sonucSutunlari) {
                sonuc.append(sutun.getAd());
                if (i++ < sorgu.sonucSutunlari.size() - 1)
                    sonuc.append(", ");
            }
            // eger donus sutunlari belirtilmemisse herseyi dondurelim.
            if (sorgu.sonucSutunlari.isEmpty())
                sonuc.append(" * ");
        }
        // tabloyu ekleyelim
        if (sorgu.tablo == null)
            throw new SQLUretimHatasi("Tablo bulunamadi..");
        sonuc.append(" from ").append(sorgu.tablo.getAd()).append(" ");

        // kisitlama zincir bileseni varsa "where" kelimesini ekleyelim.
        if (!sorgu.sutunKisitlamalari.isEmpty())
            sonuc.append(" where ");

        //eger kisitlama bileseni mevcutsa ekleyelim.
        for (SutunKisitlamaBileseni bilesen : sorgu.sutunKisitlamalari) {
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

        // tablo ve sutun uretiminde gerekli oldugundan ornek bir kavramdanTipBul uretelim. yoksa SQL uretiminde gerekli degil.
        Kavram testKavrami = new Kavram("testkavrami", Arrays.asList(new Kok("testkok", KelimeTipi.ISIM)));

        // iki test sutunu
        Sutun sutun1 = new Sutun("NUMARA", testKavrami, SutunTipi.SAYI, true);
        Sutun sutun2 = new Sutun("ISIM", testKavrami, SutunTipi.YAZI, false);
        List<Sutun> sutunlar = Arrays.asList(sutun1, sutun2);

        // test tablosu.
        sorgu.tablo = new Tablo("TEST_TABLOSU", testKavrami, sutunlar);

        // farzedelin sql sorgu sonucu 10 satir ile kisitlanmak istensin.
        sorgu.sonucMiktarKisitlamaDegeri = 10;

        // iki tane sutun kisitlama verimiz olsun. ilki "numarasi [5]'ten buyuk" kelimelerinden turemis olsun.
        BilgiBileseni bb = new BilgiBileseni("5", KiyasTipi.BUYUK);
        SutunKisitlamaBileseni numaraKisitlama = new SutunKisitlamaBileseni(sutun1, bb, BaglacTipi.YOK);
        // ikinci kisitlama verisi ise "adi [Ali] olan" kelimelerinden turemis olsun..
        bb = new BilgiBileseni("Ali", KiyasTipi.ESIT);
        SutunKisitlamaBileseni isimKisitlama = new SutunKisitlamaBileseni(sutun2, bb, BaglacTipi.VE);

        sorgu.sutunKisitlamalari = Arrays.asList(numaraKisitlama, isimKisitlama);

        SqlDonusturucu sqlDonusturucu = new MsSqlDonusturucu();

        System.out.println("sonuc:" + sqlDonusturucu.donustur(sorgu));

    }

}
