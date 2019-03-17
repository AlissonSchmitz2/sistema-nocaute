package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public abstract class AbstractWindowFrame extends JInternalFrame {
	private static final long serialVersionUID = -9124809980962961247L;

	public AbstractWindowFrame(String nomeTela, int width, int height, JDesktopPane desktop) {
		super(nomeTela, false, true, false, false);

		setLayout(null);
		setVisible(true);
		setBackground(new Color(239, 239, 239));
		setSize(width, height);

		// Abrir janela centralizada
		setLocation((desktop.getWidth() - this.getSize().width) / 2, (desktop.getHeight() - this.getSize().height) / 2);
		windowFoiMovida();
		
		// Listener janela ancestral for alterada
		addHierarchyBoundsListener(new HierarchyBoundsListener() {
			public void ancestorMoved(HierarchyEvent e) {
				windowFoiMovida();
			}

			public void ancestorResized(HierarchyEvent e) {
				windowFoiRedimensionada();
			}
		});
	}

	protected void windowFoiMovida() {
		// Extender caso queira fazer alguma a��o

		// bloquear icone
		//this.getDesktopIcon().removeMouseMotionListener(this.getDesktopIcon().getMouseMotionListeners()[0]);

		// bloquear o frame
		for (Component c : this.getComponents()) {
			if (c instanceof BasicInternalFrameTitlePane) {
				for (MouseMotionListener m : c.getMouseMotionListeners()) {
					c.removeMouseMotionListener(m);
				}
				break;
			}
		}
	}

	protected void windowFoiRedimensionada() {
		// Extender caso queira fazer alguma a��o
	}

	protected void showFrame() {
		try {
			setVisible(true);
			setMaximum(true);
			setSelected(true);
		} catch (PropertyVetoException e) {
		}
	}
}