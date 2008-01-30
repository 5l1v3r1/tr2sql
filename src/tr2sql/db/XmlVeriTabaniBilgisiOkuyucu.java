package tr2sql.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import net.zemberek.araclar.XmlYardimcisi;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class XmlVeriTabaniBilgisiOkuyucu {

    public static VeriTabani oku(String dosya) throws IOException {
        Document document = XmlYardimcisi.xmlDosyaCozumle(dosya);
        // kok elemente ulas.
        Element kokElement = document.getDocumentElement();
        String veriTabaniAdi = kokElement.getAttribute("ad");
        List<Tablo> tablolar = tablolariOku(XmlYardimcisi.ilkEleman(kokElement, "tablolar"));
        return new VeriTabani(veriTabaniAdi, tablolar);
    }

    private static List<Tablo> tablolariOku(Element tabloElementi) {
        List<Element> tabloElemanlari = XmlYardimcisi.elemanlar(tabloElementi, "tablo");
        List<Tablo> tablolar = new ArrayList<Tablo>();
        for (Element element : tabloElemanlari) {
            tablolar.add(new Tablo(
                    element.getAttribute("ad"),
                    element.getAttribute("kavram"),
                    kolonlariOku(XmlYardimcisi.ilkEleman(element, "kolonlar"))
            ));
        }
        return tablolar;
    }

    private static List<Kolon> kolonlariOku(Element kolonElementi) {
        List<Element> kolonElemanlari = XmlYardimcisi.elemanlar(kolonElementi, "kolon");
        List<Kolon> kolonlar = new ArrayList<Kolon>();
        for (Element element : kolonElemanlari) {
            String anahtarStr = element.getAttribute("anahtar").toLowerCase();
            boolean anahtar = anahtarStr.equals("true");
            kolonlar.add(new Kolon(
                    element.getAttribute("ad"),
                    element.getAttribute("kavram"),
                    KolonTipi.valueOf(element.getAttribute("tip").toUpperCase()),
                    anahtar
            ));
        }
        return kolonlar;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(XmlVeriTabaniBilgisiOkuyucu.oku("bilgi/basit-veri-tabani.xml"));

    }
}
