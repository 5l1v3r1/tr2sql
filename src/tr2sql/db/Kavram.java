/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr2sql.db;

import java.util.*;

import net.zemberek.yapi.Kok;

/**
 * @author dilek
 */
public class Kavram {

    private List<Kok> benzerKokler = new ArrayList<Kok>();
    private String ad;

    public Kavram(String kavram, List<Kok> kokListesi) {
        this.ad = kavram;
        this.benzerKokler = kokListesi;
    }

    public String getAd() {
        return ad;
    }

    public List<Kok> getBenzerKokler() {
        return benzerKokler;
    }

    public String toString() {
         return ad;
    }
}
 

