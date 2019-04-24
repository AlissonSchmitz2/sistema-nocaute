package br.com.nocaute.view;

import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {

		// Muda o Layout da janela para o padrão do Windows
		try {
			boolean hasWindowLookAndFeel = false;
			
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				// Windows
				if ("Windows".equals(info.getName())) {
					hasWindowLookAndFeel = true;
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			
			//Alternativa para outros sistemas operacionais
			if (!hasWindowLookAndFeel) {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Window.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {

			public void run() {

				new LoginWindow().setVisible(true);
				// Para testes
				// new Window().setVisible(true);
			}
		});
	}

}
