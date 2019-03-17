package br.com.nocaute.test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.nocaute.database.ConnectionFactory;
import br.com.nocaute.model.StudentModel;
import br.com.noucate.dao.StudentDAO;

public class DatabaseConnectionTest {

	public DatabaseConnectionTest(){
		Connection conn = ConnectionFactory.getConnection("master", "admin", "admin");
		
		try {
			conn.setAutoCommit(false);
			
			System.out.println("*** Conectado ao banco de dados com sucesso");
			
			// INSERT TEST
			StudentDAO dao = new StudentDAO(conn);
			
			StudentModel model = new StudentModel();
			
			model.setName("Aluno Teste x");
			model.setGenre('M');
			
			StudentModel newStudent = dao.insert(model);
			
			if (newStudent != null) {
				System.out.println("*** Novo aluno \"" + newStudent.getName() + "\" inserido com sucesso");
				
				// FIND TEST
				StudentModel foundStudent = dao.findById(newStudent.getCode());
				
				if (foundStudent != null) {
					System.out.println("*** Busca por aluno \"" + newStudent.getCode() + "\" efetuada com sucesso");
					
					// DELETE TEST
					if (dao.delete(foundStudent)) {
						System.out.println("*** Aluno: " + foundStudent.getCode() + " removido com sucesso!");
					}
				}
			}
			
			// SELECT
			List<StudentModel> list = dao.selectAll();
			
			System.out.println("*** Lista de Alunos");
			for (int i = 0; i < list.size(); i++) {				
				StudentModel student = (StudentModel) list.get(i);
				System.out.println(student.getCode() + " - " + student.getName());
			}

		} catch (SQLException e) {
			e.getErrorCode();
		}
	}
}
