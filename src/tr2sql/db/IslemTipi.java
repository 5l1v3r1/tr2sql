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
        if (kavram.getAd().equals("SORGULA"))
            return SORGULAMA;
        else if (kavram.getAd().equals("SIL"))
            return SILME;
        else if (kavram.getAd().equals("GUNCELLE"))
            return GUNCELLEME;
        else if (kavram.getAd().equals("EKLE"))
            return EKLEME;
        else return TANIMSIZ;
    }
}
