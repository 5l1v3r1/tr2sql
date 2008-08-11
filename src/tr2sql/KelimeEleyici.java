package tr2sql;

import net.zemberek.yapi.Kelime;

import java.util.List;


public interface KelimeEleyici {
    /**
     * bir ya da daha cok kelimeyi kisitlama kriterlerine gore sinar.
     * sadece kisitlama kriterleirni gecebilenleri bir liste iicnde dondurur.
     *
     * @param kelimeler: kelime dizisi
     * @return kisitlama kriterlerine uyan kelimler. eger hic bir kelime uymazsa bos liste doner.
     */
    public List<Kelime> ele(Kelime[] kelimeler);
}
