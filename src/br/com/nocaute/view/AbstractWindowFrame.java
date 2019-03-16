package br.com.nocaute.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.beans.PropertyVetoException;
import javax.swing.JInternalFrame;

public abstract class AbstractWindowFrame extends JInternalFrame {
	private static final long serialVersionUID = -9124809980962961247L;
	
	public AbstractWindowFrame(String nomeTela) {
	    super(nomeTela, false, true, false, false);
	    
	    setLayout(null);
	    setVisible(true);
	    setBorder(null);
	    setBackground(new Color(239, 239, 239));    
	    
	    //Seta tamanho
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    setBounds(new Rectangle(0, 0, screenSize.width, screenSize.height));
	    
	    //Listener janela ancestral for alterada
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
		//Extender caso queira fazer alguma ação
	}
	
	protected void windowFoiRedimensionada() {
		//Extender caso queira fazer alguma ação
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