package de.entwicklerpages.java.schoolgame.tools;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutPanel extends JPanel {

    public AboutPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setBackground(Color.WHITE);

        addCenteredLabel("<BR><FONT size=\"30\"><B>School-Game Tool</B></FONT><BR><BR>by Nicolas Hollmann<BR>Spirupi<BR>Ominöse Katze<BR><BR>");
        addCenteredLabel("This tool is part of the <I>school-game</I> project.<BR>");

        try {
            add(createLinkButton("GitHub Repository", new URI("https://github.com/Entwicklerpages/school-game")));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        addCenteredLabel("<BR>This software is licensed under the <I>GPL-3.0</I>.<BR>More information can be found in the <I>LICENSE</I> file.");
    }

    private void addCenteredLabel(String htmlText)
    {
        JLabel label = new JLabel("<HTML>" + htmlText + "</HTML>");
        label.setHorizontalAlignment(JLabel.CENTER);
        add(label);
    }

    @SuppressWarnings("SameParameterValue")
    private JButton createLinkButton(String title, final URI target)
    {
        JButton linkButton = new JButton();
        linkButton.setText("<HTML><FONT color=\"#000099\"><U>" + title + "</U></FONT></HTML>");
        linkButton.setHorizontalAlignment(SwingConstants.LEFT);
        linkButton.setBorderPainted(false);
        linkButton.setOpaque(false);
        linkButton.setBackground(Color.WHITE);
        linkButton.setToolTipText(target.toString());
        linkButton.setFocusPainted(false);
        linkButton.setHorizontalAlignment(JButton.CENTER);
        linkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (Desktop.isDesktopSupported())
                {
                    try {
                        Desktop.getDesktop().browse(target);
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });

        return linkButton;
    }

}
