package br.com.nocaute.view.tableModel;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.nocaute.model.InvoicesRegistrationModel;

public class PaymentsSituationTableRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 6493992927252298137L;
	
	// Coluna que será destacada (2 -> Valor).
	private final int columnAlter = 2;
	
	// Método responsável por colorir as células da tabela.
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// Recupera o model de acordo com a linha da tabela.
		InvoicesRegistrationModel model = ((PaymentsSituationTableModel) table.getModel()).getModel(row);
		
		JLabel celula = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		celula.setOpaque(true);
		
		// Inicia a seleção de cores.
		if (!isSelected) {
			// Se a linha nao for selecionada, muda a cor de fundo de acordo com o status da fatura
			// e muda a cor da fonte para preto
			celula.setForeground(Color.BLACK);
			
			// Pendente de pagamento. (Preto)
			if (model.getPaymentDate() == null && model.getCancellationDate() == null) {
				celula.setBackground(new Color(48, 48, 48, 100));
			} 
			// Fatura paga. (Verde)
			else if (model.getPaymentDate() != null && model.getCancellationDate() == null) {
				celula.setBackground(new Color(22, 152, 26, 100));
			} 
			// Fatura cancelada. (Amarelo)
			else if (model.getCancellationDate() != null && !model.isRegistrationFinished()) {
				celula.setBackground(new Color(244, 244, 0, 100));
			} 
			// Matrícula encerrada. (Vermelho)
			else {
				celula.setBackground(new Color(249, 0, 0, 100));
			}
			
			// Destacar coluna Valor de vermelho.
			if (model.isHighlightValue()) {
				try {
					if (column == columnAlter) {
						setBackground(new Color(249, 0, 0, 100));
					}
				} catch (Exception error) {
					error.printStackTrace();
				}
			}
            
		} else {
			// caso contrario, muda a cor de fundo para azul escuro e a fonte para a cor
			// correspondente ao status da fatura.
			celula.setBackground(new Color(65, 105, 225));
			
			// Pendente de pagamento. (Preto)
			if (model.getPaymentDate() == null && model.getCancellationDate() == null) {
				celula.setForeground(Color.BLACK);
			}
			// Fatura paga. (Verde)
			else if (model.getPaymentDate() != null && model.getCancellationDate() == null) {
				celula.setForeground(Color.GREEN);
			} 
			// Fatura cancelada. (Amarelo)
			else if (model.getCancellationDate() != null && !model.isRegistrationFinished()) {
				celula.setForeground(Color.YELLOW);
			} 
			// Matrícula encerrada. (Vermelho)
			else {
				celula.setForeground(Color.RED);
			}
		}
			
		return celula;
	}	

}
