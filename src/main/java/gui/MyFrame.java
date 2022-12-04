package gui;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        super();
        setPreferredSize(new Dimension(415, 638));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel filterPanel = new FilterPanel(this);
        add(filterPanel, BorderLayout.WEST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
