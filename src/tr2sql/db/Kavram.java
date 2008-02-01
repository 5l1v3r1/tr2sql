/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tr2sql.db;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author dilek
 */
public class Kavram {    
    
    private List<Kok> KokListesi=new ArrayList<Kok>();
    private String kavram;
           
    public Kavram (String kavram,List<Kok> KokListesi)
    {
        this.kavram=kavram;
        this.KokListesi=KokListesi;        
    }
    
    public String getKavram()
    {
        return kavram;
    }
            
    public List<Kok> getKok ()
    {
        return KokListesi;      
    }    
}  
 

