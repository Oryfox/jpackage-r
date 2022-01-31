package de.oryfox.jpackage.gui;

import de.oryfox.jpackage.JPackageF;
import lombok.Getter;
import net.sf.image4j.codec.ico.ICOEncoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

@Getter
public class MainFrame extends JFrame {

    JPanel contentPanel;

    JPanel namePanel;
    JTextField nameField;

    JPanel versionPanel;
    JTextField versionField;

    JPanel vendorPanel;
    JTextField vendorField;

    JPanel iconPanel;
    JCheckBox iconBox;
    JButton iconButton;
    File iconFile;

    JPanel copyrightPanel;
    JTextField copyrightField;

    JPanel imagePanel;
    JComboBox<String> imageBox;

    String[] WINDOWS_IMAGES = new String[]{"EXE,MSI"};
    String[] MAC_IMAGES = new String[]{"DMG,PKG"};
    String[] LINUX_IMAGES = new String[]{"DEB,RPM"};

    public MainFrame() {
        super("JPackage(r)");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        contentPanel = new JPanel(new GridLayout(0,1));

        namePanel = new JPanel(new BorderLayout());
        nameField = new JTextField();
        namePanel.add(nameField, BorderLayout.CENTER);
        namePanel.setBorder(BorderFactory.createTitledBorder("Name of the app"));
        contentPanel.add(namePanel);

        versionPanel = new JPanel(new BorderLayout());
        versionField = new JTextField();
        versionPanel.add(versionField, BorderLayout.CENTER);
        versionPanel.setBorder(BorderFactory.createTitledBorder("Version of the app"));
        contentPanel.add(versionPanel);

        vendorPanel = new JPanel(new BorderLayout());
        vendorField = new JTextField();
        vendorPanel.add(vendorField, BorderLayout.CENTER);
        vendorPanel.setBorder(BorderFactory.createTitledBorder("Vendor"));
        contentPanel.add(vendorPanel);

        iconPanel = new JPanel();
        iconBox = new JCheckBox();
        iconButton = new JButton("Select icon");
        iconButton.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog((Frame)null, "Select icon", FileDialog.LOAD);
            fileDialog.setVisible(true);
            iconFile = fileDialog.getFiles()[0];

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                var parts = iconFile.getAbsolutePath().split("\\.");
                if (!parts[parts.length - 1].equalsIgnoreCase("ico")) {
                    var builder = new StringBuilder();
                    for (int i = 0; i < parts.length - 1; i++) {
                        builder.append(parts[i]);
                        if (i != parts.length - 2) builder.append(".");
                    }
                    try {
                        var f = new File(builder + ".ico");
                        ICOEncoder.write(ImageIO.read(iconFile), f);
                        iconFile = f;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        iconPanel.add(iconBox);
        iconPanel.add(iconButton);
        iconPanel.setBorder(BorderFactory.createTitledBorder("Icon"));
        contentPanel.add(iconPanel);

        copyrightPanel = new JPanel(new BorderLayout());
        copyrightField = new JTextField();
        copyrightPanel.add(copyrightField, BorderLayout.CENTER);
        copyrightPanel.setBorder(BorderFactory.createTitledBorder("Copyright String"));
        contentPanel.add(copyrightPanel);

        imagePanel = new JPanel(new BorderLayout());
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            imageBox = new JComboBox<>(WINDOWS_IMAGES);
        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            imageBox = new JComboBox<>(MAC_IMAGES);
        } else {
            imageBox = new JComboBox<>(LINUX_IMAGES);
        }

        var saveButton = new JButton("Save App");
        saveButton.addActionListener(e -> JPackageF.writeApp());
        contentPanel.add(saveButton);

        this.getContentPane().add(contentPanel);

        pack();
        var screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(screen.width / 2 - getSize().width / 2, screen.height / 2 - getSize().height / 2);
    }
}
