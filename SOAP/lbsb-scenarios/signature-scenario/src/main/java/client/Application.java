package client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import client.ui.UI;

public class Application {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);

				UI.createAndShowGUI();
			}
		});
	}
}
