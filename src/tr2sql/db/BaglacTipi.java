package tr2sql.db;

public enum BaglacTipi {
    VE, VEYA, VIRGUL, YOK;

    public static BaglacTipi baglacTipiTahminEt(String s) {
        if (s.equals(","))
            return VIRGUL;
        else if (s.equalsIgnoreCase("ve"))
            return VE;
        else if (s.equalsIgnoreCase("veya") || s.equalsIgnoreCase("yahut"))
            return VEYA;
        else
            return YOK;
    }
}
