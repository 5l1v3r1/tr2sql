package tr2sql.db;

import net.zemberek.araclar.XmlYardimcisi;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                    sutunlariOku(XmlYardimcisi.ilkEleman(element, "sutunlar"))
            ));
        }
        return tablolar;
    }

    private List<Sutun> sutunlariOku(Element sutunElementi) {
        List<Element> sutunElemanlari = XmlYardimcisi.elemanlar(sutunElementi, "sutun");
        List<Sutun> sutunlar = new ArrayList<Sutun>();
        for (Element element : sutunElemanlari) {
            String anahtarStr = element.getAttribute("anahtar").toLowerCase();
            Kavram kavram = kavramTablosu.get(element.getAttribute("kavram"));
            boolean anahtar = anahtarStr.equals("true");
            sutunlar.add(new Sutun(
                    element.getAttribute("ad"),
                    kavram,
                    SutunTipi.valueOf(element.getAttribute("tip").toUpperCase()),
                    anahtar
            ));
        }
        return sutunlar;
    }
}
