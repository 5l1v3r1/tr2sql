package tr2sql.gui;

/**
 */
public class DemoMain {

    private AnaEkran anaEkran = new AnaEkran();

    public static void main(String[] args) {
        DemoMain demoMain = new DemoMain();
        DemoPaneli demoPaneli = new DemoPaneli(new DemoYonetici());
        demoMain.anaEkran.icerikAta(demoPaneli.getAnaPanel());
    }
}