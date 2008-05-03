package tr2sql.db;

public enum IslemTipi {
    SORGULAMA, EKLEME, GUNCELLEME, SILME;

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
}
