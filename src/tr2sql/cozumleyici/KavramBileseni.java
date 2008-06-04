package tr2sql.cozumleyici;

import tr2sql.db.Kavram;
import net.zemberek.yapi.Kelime;

public class KavramBileseni extends TemelCumleBileseni{
    Kavram kavram;

    public KavramBileseni(Kelime kelime, Kavram kavram) {
        this.kavram = kavram;
        this.tip = CumleBilesenTipi.KAVRAM;
        this.icerik = kelime.icerikStr();
        this.kelime = kelime;

    }
}
