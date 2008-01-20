package tr2sql.gui;

import javax.swing.*;
import java.awt.*;

/**
 */
public class GuiUretici {
    protected static Color background = new Color(245, 245, 255);

    private GuiUretici() {
    }

    public static JButton getRegularButton(String lbl) {
        JButton button = new JButton(lbl);
        button.setFont(new Font("Tahoma", Font.PLAIN, 11));
        return button;
    }

}
