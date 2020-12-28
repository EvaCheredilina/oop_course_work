package gui;

import javax.swing.*;
import java.awt.event.*;

public class NewSheetInfo extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel newSheetLabel;
    private JTextField groupNumberText;
    private JTextField subjectText;
    private JTextField teacherText;

    public NewSheetInfo() {
        setContentPane(contentPane);
        setLocationRelativeTo(getParent());
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();
        setVisible(true);
    }

    private void onOK() {
        if (isInputCorrect()) dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public boolean isInputCorrect() {
        return !(groupNumberText.getText().isEmpty() || subjectText.getText().isEmpty() || teacherText.getText().isEmpty());
    }

    public String getGroup() { return groupNumberText.getText(); }

    public String getSubject() { return subjectText.getText(); }

    public String getTeacher() { return teacherText.getText(); }
}
