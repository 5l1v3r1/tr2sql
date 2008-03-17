package tr2sql.db;

import net.zemberek.araclar.turkce.YaziBirimi;
import net.zemberek.araclar.turkce.YaziBirimiTipi;
import net.zemberek.araclar.turkce.YaziIsleyici;
import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;
import net.zemberek.yapi.Kelime;
import org.jmate.collections.Lists;
import org.jmate.collections.Maps;
import tr2sql.SozlukIslemleri;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TurkceSQLCozumleyici {

    private VeriTabani veriTabani;
    private Map<String, Kavram> kavramTablosu = Maps.newHashMap();

    private Zemberek zemberek;

    public TurkceSQLCozumleyici(Zemberek zemberek,
                                String veriTabaniDosyasi,
                                String kavramDosyasi) throws IOException {
        this.zemberek = zemberek;
        SozlukIslemleri sozlukIslemleri = new SozlukIslemleri(zemberek.dilBilgisi().kokler());
        KavramOkuyucu kavramOkuyucu = new KavramOkuyucu(sozlukIslemleri);

        // kavramlari okuyup tabloya at.
        Set<Kavram> kavramlar = kavramOkuyucu.oku(kavramDosyasi);
        for (Kavram kavram : kavramlar) {
            kavramTablosu.put(kavram.getAd(), kavram);
        }
        veriTabani = new XmlVeriTabaniBilgisiOkuyucu(kavramTablosu).oku(veriTabaniDosyasi);
    }

    public VeriTabani getVeriTabani() {
        return veriTabani;
    }

    public Map<String, Kavram> getKavramTablosu() {
        return kavramTablosu;
    }

    public Tablo tabloTahminEt(String giris) {
        return new BasitCumleCozumleyici(giris).tabloTahminEt();
    }


    public IslemTipi islemTipiTahminEt(String giris) {
        return new BasitCumleCozumleyici(giris).islemBul();
    }

    class BasitCumleCozumleyici {

        private List<Kelime> olasiKelimeDizisi = Lists.newArrayList();

        public BasitCumleCozumleyici(String giris) {

            // burada bir cumleden olasi kelime dizisi ortaya cikariliyor. normalde birden fazla cozum olabilir
            // biz ilk geleni seciyoruz.
            List<YaziBirimi> analizDizisi = YaziIsleyici.analizDizisiOlustur(giris);
            for (YaziBirimi birim : analizDizisi) {
                if (birim.tip == YaziBirimiTipi.KELIME) {
                    Kelime[] sonuclar = zemberek.kelimeCozumle(birim.icerik, CozumlemeSeviyesi.TUM_KOKLER);
                    if (sonuclar.length > 0)
                        olasiKelimeDizisi.add(sonuclar[0]);
                }
            }
        }

        /**
         * simdilik tek tablo buluyor.
         *
         * @return bulunursa Tablo. yoksa Exception firlatir..
         */
        public Tablo tabloTahminEt() {

            for (Kelime kelime : olasiKelimeDizisi) {
                Tablo t = veriTabani.kokeGoreTabloBul(kelime.kok());
                if (t != null)
                    return t;
            }
            return null;
        }

        /**
         * ilgili islemi bulur. simdilik sadece sorgulama kavrami ile calisiyor.
         * ve soru cumleleri ihmal ediliyor.
         *
         * @return bulunan islem tipi.
         */
        public IslemTipi islemBul() {
            Kavram sorguKavrami = kavramTablosu.get("sorgu");
            for (Kelime kelime : olasiKelimeDizisi) {
                if (sorguKavrami.kokMevcutMu(kelime.kok()))
                    return IslemTipi.SORGULAMA;
            }
            return IslemTipi.BELIRSIZ;
        }

        public List<Kelime> getOlasiKelimeDizisi() {
            return olasiKelimeDizisi;
        }
    }

}
