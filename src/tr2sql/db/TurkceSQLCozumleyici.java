package tr2sql.db;

import net.zemberek.erisim.Zemberek;
import net.zemberek.yapi.DilBilgisi;
import net.zemberek.yapi.Kelime;
import net.zemberek.yapi.Kok;
import net.zemberek.araclar.turkce.YaziBirimi;
import net.zemberek.araclar.turkce.YaziIsleyici;
import net.zemberek.araclar.turkce.YaziBirimiTipi;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.io.IOException;

import tr2sql.SozlukIslemleri;
import org.jmate.collections.Maps;


public class TurkceSQLCozumleyici {

    private VeriTabani veriTabani;
    private Map<String, Kavram> kavramTablosu = Maps.newHashMap();
    private Map<Kok, Kavram> kokKavramTablosu = Maps.newHashMap();

    private Zemberek zemberek;
    private DilBilgisi dilBilgisi;
    private SozlukIslemleri sozlukIslemleri;

    public TurkceSQLCozumleyici(Zemberek zemberek,
                                String veriTabaniDosyasi,
                                String kavramDosyasi) throws IOException {
        this.zemberek = zemberek;
        this.dilBilgisi = zemberek.dilBilgisi();
        this.sozlukIslemleri = new SozlukIslemleri(dilBilgisi.kokler());
        KavramOkuyucu kavramOkuyucu = new KavramOkuyucu(this.sozlukIslemleri);

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

    /**
     * simdilik tek tablo buluyor.
     *
     * @param giris giris cumlesi.
     * @return bulunursa Tablo. yoksa null.
     */
    public Tablo tabloTahminEt(String giris) {
        List<YaziBirimi> analizDizisi = YaziIsleyici.analizDizisiOlustur(giris);
        for (YaziBirimi birim : analizDizisi) {
            if (birim.tip == YaziBirimiTipi.KELIME) {
                Kelime[] sonuclar = zemberek.kelimeCozumle(birim.icerik, CozumlemeSeviyesi.TUM_KOKLER);
                // simdilik sadece ilk rasgelen tabloyu donduruyoruz.
                for (Kelime kelime : sonuclar) {
                    Tablo t = veriTabani.kokeGoreTabloBul(kelime.kok());
                    if (t != null)
                        return t;
                }
            }
        }
        return null;
    }
}
