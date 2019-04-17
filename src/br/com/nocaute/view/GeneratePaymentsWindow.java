package br.com.nocaute.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import br.com.nocaute.dao.InvoicesRegistrationDAO;
import br.com.nocaute.dao.PlanDAO;
import br.com.nocaute.dao.RegistrationDAO;
import br.com.nocaute.dao.RegistrationModalityDAO;
import br.com.nocaute.image.MasterImage;
import br.com.nocaute.model.InvoicesRegistrationModel;
import br.com.nocaute.model.PlanModel;
import br.com.nocaute.model.RegistrationModalityModel;
import br.com.nocaute.model.RegistrationModel;

public class GeneratePaymentsWindow extends AbstractWindowFrame {
	private static final long serialVersionUID = 7934307046018321070L;
	
	// Componentes.
	private JLabel label;
	private JButton btnGeneratePay;
	private JMonthChooser jMonthChooser;
	private JYearChooser jYearChooser;
	
	// Registration.
	private RegistrationDAO registrationDAO;
	private RegistrationModel registrationModel;
	private List<RegistrationModel> registrationsList = new ArrayList<>();
	
	// RegistrationModality.
	private RegistrationModalityDAO registrationModalityDAO;
	
	// InvoicesRegistration.
	private InvoicesRegistrationDAO invoicesRegistrationDAO;
	private InvoicesRegistrationModel invoicesRegistrationModel;
	
	// Plan.
	private PlanDAO planDAO;
	private PlanModel planModel;
	
	// Recupera a data atual.
	Date currentDate = new Date();

	public GeneratePaymentsWindow(JDesktopPane desktop) {
		super("Gerar Faturas", 270, 120, desktop);
		setFrameIcon(MasterImage.new_16x16);

		try {
			registrationDAO = new RegistrationDAO(CONNECTION);
			registrationModalityDAO = new RegistrationModalityDAO(CONNECTION);
			invoicesRegistrationDAO = new InvoicesRegistrationDAO(CONNECTION);
			planDAO = new PlanDAO(CONNECTION);
		} catch (SQLException error) {
			error.printStackTrace();
		}

		createComponents();
	}
	
	private void generatePayments() {
		int selectedMonth = jMonthChooser.getMonth();
		int selectedYear = jYearChooser.getYear();

		// Recupera a data selecionada.
		Calendar calendarAux = Calendar.getInstance();
		calendarAux.set(selectedYear, selectedMonth, 32);
		Date selectedDate = calendarAux.getTime();

		// Verifica se a data selecionada é válida.
		if (selectedDate.compareTo(currentDate) < 0) {
			bubbleWarning("A data selecionada para geração das faturas é inválida");
		} else {
			try {
				// Recupera todas as matriculas.
				registrationsList = registrationDAO.selectAll();

				// Recupera todos os dados da tabela matriculas_modalidades.
				for (int i = 0; i < registrationsList.size(); i++) {
					registrationModel = registrationsList.get(i);
					Integer registrationCode = registrationModel.getRegistrationCode();

					// Recupera a data de vencimento da fatura.
					Calendar calendar = Calendar.getInstance();
					calendar.set(selectedYear, selectedMonth, registrationModel.getExpirationDay());
					Date dueDate = calendar.getTime();

					// Recupera a lista de todas as faturas relacionadas a um determinado código de matrícula.
					List<InvoicesRegistrationModel> invoicesRegistrationsList = invoicesRegistrationDAO
							.getByRegistrationCode(registrationCode);

					// Auxiliar para comparação de datas.
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

					// Auxiliar para verificar a existência da fatura.
					boolean faturaExiste = false;

					// Percorre as faturas verificando a necessidade de inserir uma nova fatura para o aluno.
					for (int k = 0; k < invoicesRegistrationsList.size(); k++) {
						InvoicesRegistrationModel model = invoicesRegistrationsList.get(k);

						// Verifica se já existe uma fatura para a data informada.
						if (dateFormat.format(dueDate).equals(dateFormat.format(model.getDueDate()))) {
							faturaExiste = true;
							break;
						}
					}

					// Caso a fatura já exista passa para a próxima iteração do for.
					if (faturaExiste) {
						continue;
					}

					// Lista de modalidades relacionadas a um determinado codigo_matricula.
					List<RegistrationModalityModel> modalitiesList = registrationModalityDAO
							.getByRegistrationCode(registrationCode);

					// Calculos para recuperar o valor total da fatura.
					BigDecimal valorFatura;
					float valorTotal = 0;
					for (int j = 0; j < modalitiesList.size(); j++) {
						// Verifica se o aluno está matriculado na modadalide (dataFim não preenchida).
						if (modalitiesList.get(j).getFinishDate() == null) {
							planModel = planDAO.findById(modalitiesList.get(j).getPlanId());
							valorFatura = planModel.getMonthlyValue();
							valorTotal += valorFatura.floatValue();
						}
					}
					
					// Caso o valor total seja igual a 0, significa que a matrícula do aluno foi cancelada, então não gera a fatura.
					if(valorTotal == 0) {
						continue;
					}

					// Monta o objeto a ser inserido no banco.
					invoicesRegistrationModel = new InvoicesRegistrationModel();
					invoicesRegistrationModel.setRegistrationCode(registrationCode);
					invoicesRegistrationModel.setDueDate(dueDate);
					invoicesRegistrationModel.setValue(valorTotal);

					// Insere a fatura no banco de dados.
					invoicesRegistrationDAO.insert(invoicesRegistrationModel);
				}

				bubbleSuccess("Faturas geradas com sucesso");
			} catch (SQLException error) {
				error.printStackTrace();
			}
		}
	}

	private void createComponents() {

		label = new JLabel("Data de Fatura: ");
		label.setBounds(5, 10, 150, 25);
		getContentPane().add(label);

		jMonthChooser = new JMonthChooser();
		jMonthChooser.setBounds(90, 10, 150, 25);
		getContentPane().add(jMonthChooser);

		jYearChooser = new JYearChooser();
		jYearChooser.setBounds(190, 10, 50, 25);
		getContentPane().add(jYearChooser);

		btnGeneratePay = new JButton("Gerar Faturas", MasterImage.new_16x16);
		btnGeneratePay.setBounds(90, 45, 150, 25);
		btnGeneratePay.setToolTipText("Gerar as faturas");
		getContentPane().add(btnGeneratePay);

		btnGeneratePay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generatePayments();
			}
		});

	}
	
}
