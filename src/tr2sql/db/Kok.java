/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr2sql.db;

/**
 *
 * @author dilek
 */
public class Kok {
    
    private String kavram;
    private String KavramTipi;
    
    public Kok(String kavram,String KavramTipi)
    {
        this.kavram=kavram;
        this.KavramTipi=KavramTipi;
    }
    
    public String getKavram ()
    {
        return kavram;
    }
    
    public String getKavramTipi()
    {
        return KavramTipi;
    }

}
