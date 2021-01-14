package client.appClasses;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ChangePasswordView extends GridPane {

	private App_Model model;
	private App_Controller controller;
	private Stage stage;

	// 1 Controls 
	private Label lblTitle;
	private Label lblUsername;
	private Label lblCurrentUsername;
	private Label lblPassword;
	private Label lblInfo;
	// 1
	// Textfelder
	private PasswordField pwFieldPassword = new PasswordField();

	// 1 Buttons 
	protected Button btnSave = new Button("Save");
	protected Button btnCancel = new Button("Cancel");

	// Konstruktor
	public ChangePasswordView(Stage stage, App_Model model, App_Controller controller) {
		this.stage = stage;
		this.model = model;
		this.add(createDataEntryPane(), 0, 0);
		this.add(createControlPane(), 0, 1);

		getStylesheets().add(getClass().getResource("app.css").toExternalForm());
	}

	

	// 1 Data Entry Pane
	private Pane createDataEntryPane() {

		GridPane pane = new GridPane();
		pane.setId("dataEntry");
		// Declare the individual controls in the GUI
		lblTitle = new Label("Passwort ändern");
		lblTitle.getStyleClass().add("titlelabel");

		lblUsername = new Label("Username");
		lblPassword = new Label("Neues Passwort");
		lblInfo = new Label("Passwortbedingungen");
		lblInfo.getStyleClass().add("infolabel");
		
		lblCurrentUsername = new Label("");
				
		
		
		// Organize the layout, add in the controls (col, row)
		pane.add(lblTitle, 0, 0);

		pane.add(lblUsername, 0, 1);
		pane.add(lblCurrentUsername, 1, 1);

		pane.add(lblPassword, 0, 2);
		pane.add(pwFieldPassword, 1, 2);
		pane.add(lblInfo, 1, 3);

		pane.setVgap(5);
		pane.setHgap(10);
		pane.setPadding(new Insets(10, 10, 10, 10));

		return pane;
	}

	// 1 Data Control Pane
	private Pane createControlPane() {
		GridPane pane = new GridPane();
		pane.setId("controlArea");
		pane.add(btnSave, 0, 0);
		pane.add(btnCancel, 1, 0);
		pane.setHgap(10);
		pane.setPadding(new Insets(10, 10, 10, 35));
		return pane;
	}

	/*
	 * Methode um die TextFelder und Combobox zu leeren
	 */
	public void reset() {
		this.pwFieldPassword.setText("");
	}

	// Getter (Model, Controller, Stage)
	public App_Model getModel() {
		return model;
	}

	public App_Controller getController() {
		return controller;
	}
	public Stage getStage() {
		return stage;
	}

	// Setter (Model, Controller, Stage)
	public void setController(App_Controller controller) {
		this.controller = controller;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public void setModel(App_Model model) {
		this.model = model;
	}

	// Getter & Setter TextField
		
	public PasswordField getPwFieldPassword() {
		return pwFieldPassword;
	}
	public void setPwFieldPassword(PasswordField pwFieldPassword) {
		this.pwFieldPassword = pwFieldPassword;
	}
	
	//Getter & Setter Controls
	public Button getBtnSave() {
		return btnSave;
	}


	public Button getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(Button btnCancel) {
		this.btnCancel = btnCancel;
	}
	
	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	// Getter Labels
	public Label getLblTitle() {
		return lblTitle;
	}

	public Label getLblUsername() {
		return lblUsername;
	}

	public Label getLblPassword() {
		return lblPassword;
	}

	public Label getLblInfo() {
		return lblInfo;
	}
	
	public Label getLblCurrentUsername() {
		return lblCurrentUsername;
	}
	
	public void setLblCurrentUsername(String username) {
		this.lblCurrentUsername.setText(username); 
	}
	
	

}
