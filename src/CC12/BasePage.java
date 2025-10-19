
package CC12;

import javax.swing.*;

import java.awt.*;

public abstract class BasePage extends JPanel {
    protected AppFrame appFrame;

    public BasePage(AppFrame appFrame) {
        this.appFrame = appFrame;
        setLayout(new GridBagLayout());
    }

    protected GridBagConstraints createDefaultConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }
}

