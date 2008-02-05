package tr2sql.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import net.zemberek.araclar.XmlYardimcisi;
import net.zemberek.yapi.DilBilgisi;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import tr2sql.SozlukIslemleri;

public class XmlVeriTabaniBilgisiOkuyucu {

    private Map<String, Kavram> kavramTablosu;

    public XmlVeriTabaniBilgisiOkuyucu(Map<String, Kavram> kavramTablosu) {
        this.kavramTablosu = kavramTablosu;
    }

    public VeriTabani oku(String xmlVeriTabaniBilgiDosyasi) throws IOException {
        Document document = XmlYardimcisi.xmlDosyaCozumle(xmlVeriTabaniBilgiDosyasi);
        // kok elemente ulas.
        Element kokElement = document.getDocumentElement();
        String veriTabaniAdi = kokElement.getAttribute("ad");
        List<Tablo> tablolar = tablolariOku(XmlYardimcisi.ilkEleman(kokElement, "tablolar"));
        return new VeriTabani(veriTabaniAdi, tablolar);
    }

    private List<Tablo> tablolariOku(Element tabloElementi) {
        List<Element> tabloElemanlari = XmlYardimcisi.elemanlar(tabloElementi, "tablo");
        List<Tablo> tablolar = new ArrayList<Tablo>();
        for (Element element : tabloElemanlari) {
            Kavram kavram = kavramTablosu.get(element.getAttribute("kavram"));
            tablolar.add(new Tablo(
                    element.getAttribute("ad"),
                    kavram,
                    kolonlariOku(XmlYardimcisi.ilkEleman(element, "kolonlar"))
            ));
        }
        return tablolar;
    }

    private List<Kolon> kolonlariOku(Element kolonElementi) {
        List<Element> kolonElemanlari = XmlYardimcisi.elemanlar(kolonElementi, "kolon");
        List<Kolon> kolonlar = new ArrayList<Kolon>();
        for (Element element : kolonElemanlari) {
            String anahtarStr = element.getAttribute("anahtar").toLowerCase();
            Kavram kavram = kavramTablosu.get(element.getAttribute("kavram"));
            boolean anahtar = anahtarStr.equals("true");
            kolonlar.add(new Kolon(
                    element.getAttribute("ad"),
                    kavram,
                    KolonTipi.valueOf(element.getAttribute("tip").toUpperCase()),
                    anahtar
            ));
        }
        return kolonlar;
    }
}
