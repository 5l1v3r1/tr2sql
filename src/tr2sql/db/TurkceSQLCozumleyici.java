package tr2sql.db;

import net.zemberek.erisim.Zemberek;
import net.zemberek.yapi.DilBilgisi;

import java.util.Map;
import java.util.Set;
import java.io.IOException;

import tr2sql.SozlukIslemleri;
import org.jmate.collections.Maps;


public class TurkceSQLCozumleyici {

    private VeriTabani veriTabani;
    private Map<String, Kavram> kavramTablosu= Maps.newHashMap();

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
}
