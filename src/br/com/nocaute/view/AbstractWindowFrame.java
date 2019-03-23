package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import br.com.nocaute.database.ConnectionFactory;

public abstract class AbstractWindowFrame extends JInternalFrame {
	private static final long serialVersionUID = -9124809980962961247L;
	
	protected static final String CREATE_MODE = "create";
	protected static final String UPDATE_MODE = "update";
	private String formMode = CREATE_MODE;
	
	public static final Connection CONNECTION = ConnectionFactory.getConnection("master", "admin", "admin");

	public AbstractWindowFrame(String nameWindow, int width, int height, JDesktopPane desktop) {
		super(nameWindow, false, true, false, false);

		setLayout(null);
		setVisible(true);
		setBackground(new Color(239, 239, 239));
		setSize(width, height);

		// Abrir janela centralizada
		setLocation((desktop.getWidth() - this.getSize().width) / 2, (desktop.getHeight() - this.getSize().height) / 2);
		//Bloquear movimento da janela
		//windowWasMove();
		
		// Listener janela ancestral for alterada
		addHierarchyBoundsListener(new HierarchyBoundsListener() {
			public void ancestorMoved(HierarchyEvent e) {
				windowWasMove();
			}

			public void ancestorResized(HierarchyEvent e) {
				windowWasResized();
			}
		});
	}

	protected void windowWasMove() {
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

	protected void windowWasResized() {
		// Extender caso queira fazer alguma a��o
	}

	protected void showFrame() {
		try {
			setVisible(true);
			//setMaximum(true);
			setSelected(true);
		} catch (PropertyVetoException e) {
		}
	}
	
	protected boolean isCreating() {
		return formMode.equals(CREATE_MODE);
	}
	
	protected boolean isEditing() {
		return formMode.equals(UPDATE_MODE);
	}
	
	protected void setFormMode(String mode) {
		formMode = mode;
	}
	
	protected void disableComponents(List<Component> components) {
		components.forEach(component -> component.setEnabled(false));
	}
	
	protected void enableComponents(List<Component> components) {
		components.forEach(component -> component.setEnabled(true));
	}
	
	protected void clearFormFields(List<Component> components) {
		components.forEach(component -> {
			if (component instanceof JTextField) {
				((JTextField) component).setText("");
			} else if (component instanceof JTextArea) {
				((JTextArea) component).setText("");
			} else if (component instanceof JComboBox) {
				((JComboBox<?>) component).setSelectedIndex(0);
			}
		});
	}
	
	protected void bubbleSuccess(String message) {
		JOptionPane.showMessageDialog(null, message);
	}
	
	protected void bubbleError(String message) {
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.ERROR_MESSAGE, null);
	}
	
	protected void bubbleWarning(String message) {
		JOptionPane.showMessageDialog(null, message, "", JOptionPane.WARNING_MESSAGE, null);
	}
}