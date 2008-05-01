package tr2sql.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 */
public class GirisAlani {
    private JPanel mainPanel;
    private JTextArea inputArea;
    private char[] ozelKarakterler;
    private JPanel buttonPanel;

    public GirisAlani(char[] ozelKarakterler) {
        this.ozelKarakterler = ozelKarakterler;
        configure();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void setYazi(String yazi) {
        inputArea.setText(yazi);
    }

    public String getYazi() {
        return inputArea.getText();
    }

    private void configure() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new TitledBorder(new EmptyBorder(2, 2, 2, 2), "Giri\u015f alan\u0131"));
        ozelKarakterDugmeAlaniOlustur(ozelKarakterler);
        mainPanel.add(makeInputPanel(), BorderLayout.CENTER);
    }


    private JPanel makeInputPanel() {
        JPanel pq = new JPanel(new BorderLayout());
        inputArea = new JTextArea();
        inputArea.setBorder(new EmptyBorder(1, 3, 1, 3));
        inputArea.setLineWrap(true);

        // jTextPanel scroll bar icermez. O yuzden onu scroll pane'e ekliyoruz.
        JScrollPane ps = new JScrollPane();
        ps.getViewport().add(inputArea);

        pq.add(ps, BorderLayout.CENTER);
        return pq;
    }

    /**
     * Turkceye ozel karakterler icin dinamik olarak dugme uretir.
     * @param ozelKarakterler : turkce ozel karakterler.
     */
    public void ozelKarakterDugmeAlaniOlustur(char[] ozelKarakterler) {

        if (buttonPanel != null)
            mainPanel.remove(buttonPanel);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        for (char ozelKarakter : ozelKarakterler) {
            JButton button = new JButton(String.valueOf(ozelKarakter));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    String turkceChar = ((JButton) evt.getSource()).getText();
                    int pos = inputArea.getCaretPosition();
                    inputArea.insert(turkceChar, pos);
                    inputArea.setCaretPosition(pos + 1);
                    inputArea.requestFocus();
                }
            });
            buttonPanel.add(button);
        }
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.repaint();
        mainPanel.validate();
    }

}
