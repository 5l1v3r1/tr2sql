/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr2sql.db;

import net.zemberek.yapi.Kok;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dilek
 */
public class Kavram {

    private List<Kok> esKokler = new ArrayList<Kok>();
    private String ad;

    public Kavram(String kavram, List<Kok> kokListesi) {
        this.ad = kavram;
        this.esKokler = kokListesi;
    }

    public String getAd() {
        return ad;
    }

    public List<Kok> getEsKokler() {
        return esKokler;
    }

    public String toString() {
        return ad;
    }

    public boolean kokMevcutMu(Kok kok) {
        return esKokler.contains(kok);
    }
}
 

