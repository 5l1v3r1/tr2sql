package tr2sql;

import net.zemberek.yapi.Kelime;

import java.util.List;

/**
 * Kisitli dilbilgisine sahip bir sistemde kelime cozumleme sonrasinda ortaya cikan cozumlerin cogu
 * asil istedigimiz kok ve eklere sahip degildir. Bu nedenle ozellikle cozumleme sonucunda ortaya cikan
 * sonuclarin elenmesi gerekir. Bu interface konuya gore eleme islemini yapacak siniflar tarafindan kullanilir
 */
public interface KelimeEleyici {

    /**
     * bir ya da daha cok kelimeyi kisitlama kriterlerine gore sinar.
     * sadece kisitlama kriterleirni gecebilenleri bir liste iicnde dondurur.
     *
     * @param kelimeler: kelime dizisi
     * @return kisitlama kriterlerine uyan kelimler. eger hic bir kelime uymazsa bos liste doner.
     */
    List<Kelime> ele(Kelime... kelimeler);

}
