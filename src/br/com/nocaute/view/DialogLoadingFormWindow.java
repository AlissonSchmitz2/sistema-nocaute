package br.com.nocaute.view;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JLabel;

import br.com.nocaute.image.MasterImage;

public class DialogLoadingFormWindow extends JDialog {
	private static final long serialVersionUID = 7456850004230329058L;

	private JLabel label, gifLoader;
	private String msg;
	private boolean closeLoading = false;

	public DialogLoadingFormWindow(String msg) {

		this.msg = msg;

		setTitle("Aguarde");
		setLayout(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(new Dimension(500, 170));
		setLocationRelativeTo(null);
		setResizable(false);
		// setModal(true);

		gifLoader = new JLabel(MasterImage.loading);
		gifLoader.setBounds(15, 10, 100, 100);
		add(gifLoader);

		label = new JLabel();
		label.setBounds(160, 35, 250, 50);
		add(label);

		initThread();
	}

	public void initThread() {

		label.setText(getMsg());

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {

						if (!isVisible()) {
							setVisible(true);
						}

						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							throw e;
						}

						if (isCloseLoading()) {
							dispose();
							break;
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "Loading.LoadingSistemaNocaute").start();
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isCloseLoading() {
		return closeLoading;
	}

	public void setCloseLoading(boolean closeLoading) {
		this.closeLoading = closeLoading;
	}
}
