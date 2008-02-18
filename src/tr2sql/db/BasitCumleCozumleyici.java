package tr2sql.db;

import net.zemberek.erisim.Zemberek;
import net.zemberek.yapi.Kelime;
import net.zemberek.araclar.turkce.YaziBirimi;
import net.zemberek.araclar.turkce.YaziIsleyici;
import net.zemberek.araclar.turkce.YaziBirimiTipi;
import net.zemberek.islemler.cozumleme.CozumlemeSeviyesi;

import java.util.List;

import org.jmate.collections.Lists;

public class BasitCumleCozumleyici {

    Zemberek zemberek;
    String giris;

    Kelime yuklem;

    List<Kelime[]> olasiKelimeDizisi = Lists.newArrayList();

    public BasitCumleCozumleyici(Zemberek zemberek, String giris) {
        this.zemberek = zemberek;
        this.giris = giris;

        List<YaziBirimi> analizDizisi = YaziIsleyici.analizDizisiOlustur(giris);
        for (YaziBirimi birim : analizDizisi) {
            if (birim.tip == YaziBirimiTipi.KELIME) {
                Kelime[] sonuclar = zemberek.kelimeCozumle(birim.icerik, CozumlemeSeviyesi.TUM_KOKLER);
                if(sonuclar.length>0)
                olasiKelimeDizisi.add(sonuclar);
            }
        }
    }
}
