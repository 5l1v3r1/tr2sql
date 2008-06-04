package tr2sql.db;

public enum KiyasTipi {
    ESIT,
    ESIT_DEGIL,
    BASI_BENZER,
    BASI_BENZEMEZ,
    SONU_BENZER,
    SONU_BENZEMEZ,
    ARA_BENZER,
    ARA_BENZEMEZ,
    KUCUK,
    BUYUK,
    KUCUK_ESIT,
    BUYUK_ESIT,
    NULL,
    NULL_DEGIL,
    DAHIL,
    DAHIL_DEGIL;

    public KiyasTipi tersi() {
        switch (this) {
            case BUYUK:
                return KUCUK_ESIT;
            case KUCUK:
                return BUYUK_ESIT;
            case BUYUK_ESIT:
                return KUCUK;
            case KUCUK_ESIT:
                return BUYUK;
            case ESIT:
                return ESIT_DEGIL;
            case ESIT_DEGIL:
                return ESIT;
            case NULL:
                return NULL_DEGIL;
            case BASI_BENZER:
                return BASI_BENZEMEZ;
            case BASI_BENZEMEZ:
                return BASI_BENZER;
            case SONU_BENZER:
                return SONU_BENZEMEZ;
            case SONU_BENZEMEZ:
                return SONU_BENZER;
            case ARA_BENZER:
                return ARA_BENZEMEZ;
            case ARA_BENZEMEZ:
                return ARA_BENZER;
            case DAHIL:
                return DAHIL_DEGIL;
            case DAHIL_DEGIL:
                return DAHIL;
        }
        throw new IllegalArgumentException("beklenmeyen deger!");
    }
}
