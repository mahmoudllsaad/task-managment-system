package task.managment.pojo;

import javafx.beans.property.SimpleStringProperty;

public class User {

		private final SimpleStringProperty userName;
		private final SimpleStringProperty userEmail;
		private final SimpleStringProperty userPhone;
		private final SimpleStringProperty rule;

		public User(String userName, String userEmail, String userPhone, String rule) {
			this.userName = new SimpleStringProperty(userName);
			this.userEmail = new SimpleStringProperty(userEmail);
			this.userPhone = new SimpleStringProperty(userPhone);
			this.rule = new SimpleStringProperty(rule);
		}
		
		public void setUserName(String UserName) {
			this.userName.set(UserName);
		}

		public String getUserName() {
			return userName.get();
		}
		
		public void setUserEmail(String UserEmail) {
			this.userEmail.set(UserEmail);
		}

		public String getUserEmail() {
			return userEmail.get();
		}
		
		public void setUserPhone(String UserPhone) {
			this.userPhone.set(UserPhone);
		}

		public String getUserPhone() {
			return userPhone.get();
		}
		
		public void setUserRule(String UserRule) {
			this.rule.set(UserRule);
		}

		public String getRule() {
			return rule.get();
		}

		

}
