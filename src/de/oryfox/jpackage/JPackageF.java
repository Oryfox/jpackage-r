package de.oryfox.jpackage;

import com.formdev.flatlaf.FlatLightLaf;
import de.oryfox.jpackage.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class JPackageF {

    static MainFrame frame;

    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put( "Button.arc", 999 );
        UIManager.put( "TextComponent.arc", 999 );

        var file = readFile();

        if (!file.getName().contains(".jar")) {
            System.exit(0);
        }

        frame = new MainFrame();
        frame.setVisible(true);
    }

    public static File readFile() {
        FileDialog fileDialog = new FileDialog((Frame) null, "Select jar file to package", FileDialog.LOAD);
        fileDialog.setDirectory(System.getProperty("user.home"));
        fileDialog.setMultipleMode(false);
        fileDialog.setVisible(true);
        var files =  fileDialog.getFiles();
        if (files.length > 0) return files[0];
        System.exit(0);
        throw new RuntimeException();
    }

    public static void writeApp() {

    }
}
