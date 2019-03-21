package br.com.nocaute.view;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.text.MaskFormatter;

import br.com.nocaute.table.model.StudentRegistrationTableModel;

public class StudentRegistrationWindow extends AbstractGridWindow{
	private static final long serialVersionUID = -4201960150625152379L;
	
	// Componentes
	private JButton btnBuscar, btnAdicionar, btnRemover, btnSalvar, btnAddModalidade;
	private JLabel label;
	private JTextField txfMatricula, txfAluno, txfAlunoDescricao;
	private JFormattedTextField txfDtMatricula, txfVencFatura;

	private StudentRegistrationTableModel model;
	private JTable jTableRegistration;

	// Icones
	private ImageIcon iconBuscar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/localizar.png"));
	private ImageIcon iconAdicionar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/adicionar.png"));
	private ImageIcon iconRemover = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/remover.png"));
	private ImageIcon iconSalvar = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/22x22/salvar.png"));
	private ImageIcon iconJanela = new ImageIcon(
			this.getClass().getResource("/br/com/nocaute/image/16x16/estudante.png"));

	public StudentRegistrationWindow(JDesktopPane desktop) {
			super("Matricular Aluno", 450, 380, desktop);
			setFrameIcon(iconJanela);
			
			criarComponentes();
		}

	private void criarComponentes() {

		btnBuscar = new JButton("Buscar");
		btnBuscar.setBounds(15, 5, 95, 40);
		btnBuscar.setIcon(iconBuscar);
		btnBuscar.setToolTipText("Clique aqui para buscar os usu�rios");
		getContentPane().add(btnBuscar);

		btnAdicionar = new JButton("Adicionar");
		btnAdicionar.setBounds(110, 5, 110, 40);
		btnAdicionar.setIcon(iconAdicionar);
		btnAdicionar.setToolTipText("Clique aqui para adicionar um usu�rio");
		getContentPane().add(btnAdicionar);

		btnRemover = new JButton("Remover");
		btnRemover.setBounds(220, 5, 110, 40);
		btnRemover.setIcon(iconRemover);
		btnRemover.setToolTipText("Clique aqui para remover");
		getContentPane().add(btnRemover);

		btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(330, 5, 95, 40);
		btnSalvar.setIcon(iconSalvar);
		btnSalvar.setToolTipText("Clique aqui para salvar");
		getContentPane().add(btnSalvar);

		label = new JLabel("Matr�cula: ");
		label.setBounds(5, 55, 50, 25);
		getContentPane().add(label);

		txfMatricula = new JTextField();
		txfMatricula.setBounds(90, 55, 70, 20);
		getContentPane().add(txfMatricula);

		label = new JLabel("Aluno: ");
		label.setBounds(5, 80, 150, 25);
		getContentPane().add(label);
		
		txfAluno = new JTextField();
		txfAluno.setBounds(90, 80, 70, 20);
		txfAluno.setBackground(Color.yellow);
		txfAluno.setText("Teclar F9");
		txfAluno.setEnabled(false);
		txfAluno.setToolTipText("Pressione F9 para buscar o aluno");
		getContentPane().add(txfAluno);
		
		txfAlunoDescricao = new JTextField();
		txfAlunoDescricao.setBounds(165, 80, 258, 20);
		txfAlunoDescricao.setEnabled(false);
		txfAlunoDescricao.setToolTipText("Nome do aluno");
		getContentPane().add(txfAlunoDescricao);
		
		label = new JLabel("Data Matr�cula: ");
		label.setBounds(5, 105, 150, 25);
		getContentPane().add(label);
		/*
		JDateChooser jDateChooser = new JDateChooser(new Date());
		jDateChooser.setBounds(90, 105, 90, 20);
		jDateChooser.setDateFormatString("dd/MM/yyyy");
		getContentPane().add(jDateChooser);
		jDateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// Recuperar data do campo. Esse m�todo � chamado 
				// na primeira vez que executa o sistema
				//SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy"); 
				//String novaData = formatador.format(jDateChooser.getDate()); 
				//System.out.println(novaData);
			}
		});
		
		*/
		label = new JLabel("Dia do vencimento da fatura: ");
		label.setBounds(223, 105, 150, 25);
		getContentPane().add(label);
		
		try {
			txfDtMatricula = new JFormattedTextField(new MaskFormatter("  ## /  ##  / ####       "));
			txfDtMatricula.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfDtMatricula.setBounds(90, 105, 90, 20);
			txfDtMatricula.setToolTipText("Data da matr�cula");
			getContentPane().add(txfDtMatricula);
			
			txfVencFatura = new JFormattedTextField(new MaskFormatter("#######"));
			txfVencFatura.setFocusLostBehavior(JFormattedTextField.COMMIT);
			txfVencFatura.setBounds(373, 105, 50, 20);
			getContentPane().add(txfVencFatura);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		btnAddModalidade = new JButton("Adicionar Modalidade");
		btnAddModalidade.setBounds(5, 140, 150, 23);
		btnAddModalidade.setToolTipText("Clique aqui para adicionar uma modalidade");
		getContentPane().add(btnAddModalidade);
		
		carregarGrid();
	}
	
	private void carregarGrid() {
		model = new StudentRegistrationTableModel();
		jTableRegistration = new JTable(model);

		// Habilita a sele��o por linha
		jTableRegistration.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		grid = new JScrollPane(jTableRegistration);
		setLayout(null);
		redimensionarGrid(grid, 5, 170, 420, 170);
		grid.setVisible(true);

		add(grid);
	}
}
