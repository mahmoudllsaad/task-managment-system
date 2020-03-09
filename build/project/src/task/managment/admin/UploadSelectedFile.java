package task.managment.admin;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Iterator;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.control.Alert;

import javafx.scene.control.Alert.AlertType;
import task.managment.database.connection.MySqlDatabaseConnection;
import task.managment.encrypt.utils.PasswordUtils;

public class UploadSelectedFile {

	public void setExcelFilePath(String path) {
		if (path != null)
			if (!path.equals(""))
		UploadFile(path);

	}
	
	public boolean validateComponent(String userName, String password, String email, double phone, double ruleId) {

		boolean matches = false;
		
		matches = Pattern.compile("[12]").matcher(String.valueOf((int)ruleId)).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Enter Valid Rule id ");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("^[a-zA-Z0-9]{6,20}$").matcher(userName).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Min length 6 max 20 A to Z 0 To 9 ");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("^(.+)@(.+)$").matcher(email).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("Email valide format ex@info.com ");
			alert.showAndWait();

			return matches;
		}

		matches = Pattern.compile("\\d{9}").matcher(String.valueOf((int)phone)).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("phone valid format 9 numeric length ");
			alert.showAndWait();

			return matches;
		}
		
		matches = Pattern.compile("^(?=.*[0-9])(?=\\S+$).{8,}$").matcher(password).matches();

		if (!matches) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("Enter correct Value");
			alert.setContentText("password valid format at least 8 length no whitespace digit must occur at least once ex:esdcvf4b ");
			alert.showAndWait();

			return matches;
		}

		return matches;

	}


	private void UploadFile(String path) {

		int batchSize = 20;

		Connection connection = null;

		try {

			FileInputStream inputStream = new FileInputStream(path);

			Workbook workbook = new XSSFWorkbook(inputStream);

			Sheet firstSheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = firstSheet.iterator();

			connection = MySqlDatabaseConnection.getConnection();
			connection.setAutoCommit(false);

			String sql = "INSERT INTO USER (USER_NAME, PASSWORD, USER_EMAIL,USER_PHONE,RULES_ID) VALUES (?,?,?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);

			int count = 0;

			rowIterator.next(); // skip the header row

			while (rowIterator.hasNext()) {
				Row nextRow = rowIterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				
				boolean valid = validateComponent(nextRow.getCell(0).getStringCellValue(),
						nextRow.getCell(1).getStringCellValue(),
						nextRow.getCell(2).getStringCellValue(), 
						nextRow.getCell(3).getNumericCellValue(),
						nextRow.getCell(4).getNumericCellValue());
				if(!valid)
					return;

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();

					int columnIndex = nextCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						String userName = nextCell.getStringCellValue();
						statement.setString(1, userName);
						break;
					case 1:
						String password = nextCell.getStringCellValue();
						String salt = PasswordUtils.getSalt(30);
					    String securePassword = PasswordUtils.generateSecurePassword(password, salt);
						statement.setString(2, securePassword.concat("-").concat(salt));
						break;
					case 2:
						String userEmail = nextCell.getStringCellValue();
						statement.setString(3, userEmail);
						break;
					case 3:
						int userPhone = (int) nextCell.getNumericCellValue();
						statement.setInt(4, userPhone);
						break;
					case 4:
						int ruleId = (int) nextCell.getNumericCellValue();
						statement.setInt(5, ruleId);
					
					}

				}

				statement.addBatch();

				if (count % batchSize == 0) {
					statement.executeBatch();
				}

			}

			workbook.close();

			// execute the remaining queries
			statement.executeBatch();

			connection.commit();
			connection.close();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Sucess");
			alert.setHeaderText("File Uploaded on The Database");
			alert.setContentText("File Upoaded Sucessfully !");
			alert.showAndWait();

		} catch (IOException ex1) {
			System.out.println("Error reading file");
			ex1.printStackTrace();
		} catch (SQLException ex2) {
			System.out.println("Database error");
			ex2.printStackTrace();
		}

	}
}
