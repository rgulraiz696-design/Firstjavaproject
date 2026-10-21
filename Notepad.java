
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Notepad extends JFrame
        implements WindowListener, MouseListener {

    JTextArea textArea;
    JMenuItem open, save, exit;
    File currentFile = null;
    String savedText = "";

    public Notepad() {
        setTitle("My Notepad");
        setSize(600, 400);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");

        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");

        file.add(open);
        file.add(save);
        file.add(exit);
        bar.add(file);
        setJMenuBar(bar);

        open.addMouseListener(this);
        save.addMouseListener(this);
        exit.addMouseListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    void openFile() {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showOpenDialog(this)
                == JFileChooser.APPROVE_OPTION) {
            currentFile = chooser.getSelectedFile();

            try (BufferedReader reader =
                    new BufferedReader(new FileReader(currentFile))) {

                textArea.setText("");
                String line;

                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }

                savedText = textArea.getText();
                setTitle("My Notepad - " + currentFile.getName());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error opening file!");
            }
        }
    }

    void saveFile() {
        JFileChooser chooser = new JFileChooser();

        if (currentFile == null) {
            if (chooser.showSaveDialog(this)
                    != JFileChooser.APPROVE_OPTION) {
                return;
            }
            currentFile = chooser.getSelectedFile();
        }

        try (BufferedWriter writer =
                new BufferedWriter(new FileWriter(currentFile))) {

            writer.write(textArea.getText());
            savedText = textArea.getText();

            setTitle("My Notepad - " + currentFile.getName());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving file!");
        }
    }

    // MouseListener
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == open) {
            openFile();
        } else if (e.getSource() == save) {
            saveFile();
        } else if (e.getSource() == exit) {
            closeNotepad();
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // Check unsaved changes
    void closeNotepad() {
        if (!textArea.getText().equals(savedText)) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "good bye?",
                    "Unsaved Changes",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                saveFile();

                if (!textArea.getText().equals(savedText)) {
                    return;
                }

            } else if (choice == JOptionPane.CANCEL_OPTION
                    || choice == JOptionPane.CLOSED_OPTION) {
                return;
            }
        }

        dispose();
    }

    // WindowListener
    public void windowClosing(WindowEvent e) {
        closeNotepad();
    }

    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Notepad());
    }
}
