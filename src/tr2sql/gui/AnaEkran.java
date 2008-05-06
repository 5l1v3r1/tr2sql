package tr2sql.gui;

import javax.swing.*;
import java.awt.*;

/**
 */
public class AnaEkran extends JFrame {
    private JPanel gosterilenIcerik = null;

    public AnaEkran() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container cp = getContentPane();
        cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
        setTitle("tr2sql");
    }

    public void icerikAta(JPanel panel) {
        if (gosterilenIcerik != null) {
            this.remove(gosterilenIcerik);
        }
        gosterilenIcerik = panel;
        add(panel);
        validate();
        setPreferredSize(new Dimension(640, 480));
        setLocation(100, 100);
        pack();
        setVisible(true);
    }
}
