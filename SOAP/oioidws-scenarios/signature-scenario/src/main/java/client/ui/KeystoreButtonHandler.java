package client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class KeystoreButtonHandler implements ActionListener {
	private JFileChooser fc = new JFileChooser();
	private UI ui;

	public KeystoreButtonHandler(UI ui) {
		this.ui = ui;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int returnVal = fc.showOpenDialog(ui);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ui.setFile(fc.getSelectedFile());

			JPasswordField passwordField = new JPasswordField();
			int ok = JOptionPane.showConfirmDialog(ui, passwordField, "Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

			if (ok == JOptionPane.OK_OPTION) {
				ui.setPassword(new String(passwordField.getPassword()));
				ui.writeLog("Loaded keystore");
			}
			else {
				ui.writeLog("Please enter a password for the keystore");
			}
		}
	}
}
