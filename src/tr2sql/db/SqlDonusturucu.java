package tr2sql.db;

/**
 * Bu interface'e sahip tum siniflarin SorguTasiyici bilgisi ile gercek bir SQL cumlesi olusturmasi gerekir.
 */
public interface SqlDonusturucu {
    /**
     * verilen bir SorguTasiyici nesnesindeki degerleri kullanarak ilgili gercek SQL cumlesini uretir.
     * @param sorgu Sorgu bilgilerinin tasindigi veri yapisi
     * @return olusturulan SQL cumlesi. Eger donusum yapilamazsa bos String "" doner.
     */
    String donustur(SorguTasiyici sorgu);
}
