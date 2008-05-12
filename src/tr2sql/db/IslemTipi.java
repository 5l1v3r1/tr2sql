package tr2sql.db;

public enum IslemTipi {
    SORGULAMA, EKLEME, GUNCELLEME, SILME, TANIMSIZ;

    public String sqlDonusumu() {
        switch (this) {
            case SORGULAMA:
                return "select ";
            case EKLEME:
                return "insert ";
            case GUNCELLEME:
                return "update ";
            case SILME:
                return "delete";
        }
        throw new IllegalArgumentException("Islem tipi icin SQL donusum uretilemedi. Program hatasi.");
    }

    public static IslemTipi kavramaGoreIslem(Kavram kavram) {
        if (kavram.getAd().equals("sorgu"))
            return SORGULAMA;
        else if (kavram.getAd().equals("sil"))
            return SILME;
        else if (kavram.getAd().equals("guncelle"))
            return GUNCELLEME;
        else if (kavram.getAd().equals("ekle"))
            return EKLEME;
        else return TANIMSIZ;
    }
}
