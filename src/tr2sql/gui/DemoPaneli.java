package tr2sql.gui;

import net.zemberek.araclar.turkce.YaziIsleyici;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 */
public class DemoPaneli {

    private JPanel anaPanel;
    private GirisAlani girisAlani;
    private tr2sql.gui.CikisAlani cikisAlani = new tr2sql.gui.CikisAlani();
    private DemoYonetici dy;
    private File currentDir = null;
    public static final int MAX_LOAD_STRING_BOY = 32000;


    public DemoPaneli(DemoYonetici dy) {
        this.dy = dy;
        configure();
    }

    public void yaziEkle(String text) {
        girisAlani.setYazi(text);
    }

    public JPanel getAnaPanel() {
        return anaPanel;
    }

    public void configure() {
        // ana paneli ve islem dugmelerinin yer alacagi paneli olustur
        anaPanel = new JPanel();
        JPanel buttonPanel = makeButtonPanel();
        // Islem dugmelerini kuzeye yerlestir
        anaPanel.setLayout(new BorderLayout());
        anaPanel.add(buttonPanel, BorderLayout.NORTH);

        //giris ve cikisin pencere buyudugunde ayni ende kalmasi icin onlari ayrica Grid Layout'a
        //sahip bir panele yerlestir. sonucta her ikisinide ana panelin merkezine koy.
        JPanel ioPanel = new JPanel(new GridLayout(2,1));
        girisAlani = new GirisAlani(dy.ozelKarakterDizisiGetir());
        ioPanel.add(girisAlani.getMainPanel());
        ioPanel.add(cikisAlani.getMainPanel());
        anaPanel.add(ioPanel, BorderLayout.CENTER);
    }


    public JPanel makeButtonPanel() {
        JPanel pt = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());

        JPanel centerPanel = new JPanel(new FlowLayout());

        JButton btnLoad = GuiUretici.getRegularButton("Y\u00fckle");
        btnLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                JFileChooser c;
                if (currentDir == null)
                    c = new JFileChooser();
                else
                    c = new JFileChooser(currentDir);

                int rVal = c.showOpenDialog(anaPanel);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File f = c.getSelectedFile();
                        String yazi = YaziIsleyici.yaziOkuyucu(f.toString());
                        girisAlani.setYazi(yazi);
                        currentDir = f;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        topPanel.add(btnLoad);

        JButton btnClear;
        btnClear = GuiUretici.getRegularButton("Sil");
        btnClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                girisAlani.setYazi("");
                cikisAlani.setYazi("");
            }
        });
        topPanel.add(btnClear);

        pt.add(topPanel, BorderLayout.NORTH);

        JButton btnDenetle;
        btnDenetle = GuiUretici.getRegularButton("Denetle");
        btnDenetle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.yaziDenetle(girisAlani.getYazi()));
            }
        });
        centerPanel.add(btnDenetle);

        JButton btnCozumle;
        btnCozumle = GuiUretici.getRegularButton("\u00c7\u00f6z\u00fcmle");
        btnCozumle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.yaziCozumle(girisAlani.getYazi()));
            }
        });
        centerPanel.add(btnCozumle);

        JButton kisitliCozumleBtn;
        kisitliCozumleBtn = GuiUretici.getRegularButton("Kisitli \u00c7\u00f6z\u00fcmle");
        kisitliCozumleBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.kisitliCozumle(girisAlani.getYazi()));
            }
        });
        centerPanel.add(kisitliCozumleBtn);

        JButton btnDeascii;
        btnDeascii = GuiUretici.getRegularButton("Ascii->Tr");
        btnDeascii.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.asciiToTurkce(girisAlani.getYazi()));
            }
        });
        centerPanel.add(btnDeascii);

        JButton btnascii;
        btnascii = GuiUretici.getRegularButton("Tr->Ascii");
        btnascii.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.turkceToAscii(girisAlani.getYazi()));
            }
        });
        centerPanel.add(btnascii);

        JButton btnHecele;
        btnHecele = GuiUretici.getRegularButton("Hecele");
        btnHecele.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.hecele(girisAlani.getYazi()));
            }
        });
        centerPanel.add(btnHecele);

        JButton btnOner;
        btnOner = GuiUretici.getRegularButton("\u00d6ner");
        btnOner.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cikisAlani.setYazi(dy.oner(girisAlani.getYazi()));
            }
        });
        centerPanel.add(btnOner);

        pt.add(centerPanel, BorderLayout.CENTER);

        return pt;

    }

}
