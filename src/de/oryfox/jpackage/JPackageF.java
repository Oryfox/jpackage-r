package de.oryfox.jpackage;

import com.formdev.flatlaf.FlatLightLaf;
import de.oryfox.jpackage.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class JPackageF {

    static MainFrame frame;
    public static File file;

    public static void main(String[] args) {
        FlatLightLaf.setup();
        UIManager.put( "Button.arc", 999 );
        UIManager.put( "TextComponent.arc", 999 );

        file = readFile();

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
        var mac = System.getProperty("os.name").toLowerCase().contains("mac");
        var processBuilder = new ProcessBuilder((!mac ? "\"" : "") + frame.getExeFile().getAbsolutePath() + (!mac ? "\"" : ""), "--input", "\"" + file.getParent() + "\"", "--name", "\"" + frame.getNameField().getText() + "\"", "--type", (String) frame.getImageBox().getSelectedItem(), "--app-version", "\"" + frame.getVersionField().getText() + "\"", "--copyright", "\"" + frame.getCopyrightField().getText() + "\"", "--vendor", "\"" + frame.getVendorField().getText() + "\"" + (frame.getIconBox().isSelected() ? " --icon " + "\"" + frame.getIconFile().getAbsolutePath() + "\"" : ""), "--main-jar", "\"" + file.getAbsolutePath() + "\"", "--main-class", "\"" + frame.getMainClassField().getText() + "\"", (System.getProperty("os.name").toLowerCase().contains("win") ? "--win-menu" : ""));
        var stringBuilder = new StringBuilder();
        processBuilder.command().forEach(s -> stringBuilder.append(s).append(" "));
        System.out.println(stringBuilder);
        System.out.println();
        if (mac) {
            var selection = new StringSelection(stringBuilder.toString());
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
            JOptionPane.showMessageDialog(frame, "Copied to system clipboard!", "Copied command", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
        processBuilder.redirectErrorStream(true);
        processBuilder.directory(file.getParentFile());
        try {
            var process = processBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ( (line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            System.out.println("Finished");
            System.exit(0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
