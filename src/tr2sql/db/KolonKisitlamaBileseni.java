package tr2sql.db;

/**
 * Bu sinif kolon kisitlama verisini tasir. Ornegin "numarasi [5] olan" kelimelerinden
 * Kolon = NUMARA, kisitlamaDegeri="5" ve KiyasTipi=ESITLIK verielri ile bu siniftan bir nesne olusturulmasi gerekir.
 */
public class KolonKisitlamaBileseni {
    public Kolon kolon;
    public String kisitlamaDegeri;
    public KiyasTipi kiyasTipi;

    public KolonKisitlamaBileseni(Kolon kolon, String kisitlamaDegeri, KiyasTipi kiyasTipi) {
        this.kolon = kolon;
        this.kisitlamaDegeri = kisitlamaDegeri;
        this.kiyasTipi = kiyasTipi;
    }
}
