package tr2sql.cozumleyici;

import tr2sql.db.Kolon;
import tr2sql.db.KolonKisitlamaBileseni;
import tr2sql.db.SQLUretimHatasi;
import tr2sql.db.SorguTasiyici;

import java.util.ArrayList;
import java.util.List;

public class BasitDurumMakinesi {

    public enum Durum {
        BASLA,
        KOLON_ALINDI,
        BILGI_ALINDI,
        KIYAS_ALINDI,
        OLMAK_ALINDI,
        TABLO_BULUNDU,
        SINIR_BELIRLENDI,
        ISLEM_BELIRLENDI,
        SON
    }

    private Durum suAnkiDurum = Durum.BASLA;
    private SorguTasiyici sorguTasiyici = new SorguTasiyici();

    private List<CumleBileseni> bilesenler;

    private List<KolonKisitlamaBileseni> kolonBilesenleri =
            new ArrayList<KolonKisitlamaBileseni>();

    private List<Kolon> kolonlar = new ArrayList<Kolon>();
    private List<String> bilgiler = new ArrayList<String>();


    public BasitDurumMakinesi(List<CumleBileseni> bilesenler) {
        this.bilesenler = bilesenler;
    }

    public SorguTasiyici islet() {
        for (int i = 0; i < bilesenler.size(); i++) {

            CumleBileseni bilesen = bilesenler.get(i);
            suAnkiDurum = gecis(bilesen);
        }
        return sorguTasiyici;
    }

    private Durum gecis(CumleBileseni bilesen) {

        CumleBilesenTipi gecis = bilesen.tip();

        switch (suAnkiDurum) {

            case BASLA:
                switch (gecis) {
                    case KOLON:
                        KolonBileseni kb = (KolonBileseni) bilesen;
                        kolonlar.add(kb.getKolon());
                        if (kb.baglacVar())
                            return Durum.KOLON_ALINDI;
                    case TABLO:
                        TabloBileseni tabloBil = (TabloBileseni) bilesen;
                        sorguTasiyici.tablo = tabloBil.getTablo();
                        return Durum.TABLO_BULUNDU;
                }
                break;

            case KOLON_ALINDI:
                switch (gecis) {
                    case KISITLAMA_BILGISI:
                        BilgiBileseni bil = ((BilgiBileseni) bilesen);
                        bilgiler.add(bil.icerik());
                        return Durum.BILGI_ALINDI;
                    case KOLON:
                        break;
                }
                break;

            case BILGI_ALINDI:
                switch (gecis) {
                    case OLMAK:
                        break;
                    case KIYASLAYICI:
                        break;
                    case KOLON:
                        break;
                    case KISITLAMA_BILGISI:
                        break;
                }
                break;

            case OLMAK_ALINDI:
                switch (gecis) {
                    case KOLON:
                        break;
                }
                break;

            case KIYAS_ALINDI:
                switch (gecis) {

                }
                break;

            case TABLO_BULUNDU:
                switch (gecis) {
                    case ISLEM:
                        IslemBileseni b = (IslemBileseni) bilesen;
                        sorguTasiyici.islemTipi = b.getIslem();
                        return Durum.ISLEM_BELIRLENDI;
                }
                break;
        }
        throw new SQLUretimHatasi("Beklenmeyen cumle bilseni: [" + bilesen.toString() + "]. " +
                "Su anki durum:" + suAnkiDurum.name());
    }

    private void kolonKisitlamaIsle() {
        for (Kolon kolon : kolonlar) {
            for (String s : bilgiler) {

            }
        }
    }

    private class BilgiKiyasIkilisi {
        String bilgi;

    }
}