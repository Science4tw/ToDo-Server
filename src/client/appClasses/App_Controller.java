package client.appClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;
import client.ServiceLocator;
import client.abstractClasses.Controller;
import client.commonClasses.Translator;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import server.Priority;
import server.ToDo;
import testOrGarbage.App_Model;

public class App_Controller extends Controller<App_Model, App_View> {
	
	private Socket socket = null;
	
	private BufferedWriter bufferedWriter = null;
	private OutputStreamWriter outputStreamWriter = null;
	
	private BufferedReader bufferedReader = null;
	private InputStreamReader inputStreamReader = null;

	private boolean connected = false;
	
	ServiceLocator serviceLocator;

	// Speichert Wert für gültige Textfelder
	private boolean usernameValid = false;
	private boolean passwordValid = false;

	// Konstruktor
	public App_Controller(App_Model model, App_View view) {
		super(model, view);

		
		
		
		/**
		 * DONE Connect Client with the Server Button Connect
		 */
		view.getBtnConnect().setOnAction(event -> {
			System.out.println("User klickte: 'connect'");
			System.out.println(view.getTxtIP().getText());
			System.out.println(view.getTxtPort().getText());
			connect(view.getTxtIP().getText(), Integer.parseInt(view.getTxtPort().getText()));
			
		});

		/**
		 * DONE CreateLogin Button Save in CreateAccountView
		 */
		view.getCreateAccountView().getBtnSave().setOnAction(event -> {
			try {
				System.out.println("User klickte: 'create Account' (CreateLogin)");
				createLogin(view.getCreateAccountView().getTxtUsername().getText(),
						view.getCreateAccountView().getTxtPassword().getText());
				view.getStage().setScene(getMainScene());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		/**
		 * TODO Login
		 */
		view.getBtnLogin().setOnAction(event -> {
			login(view.getTxtUsername().getText(), view.getTxtPassword().getText());
		});

//		model.getToDos();

		// *** SZENEN WECHSEL ***
		// Szenenwechsel bei Create ToDo Button in appview
		view.getBtnCreateToDo().setOnAction(event -> {
			view.getStage().setScene(view.getCreateToDoScene());
		});
		// Szenenwechsel bei klick auf Menu create account
		view.getMenuAccountCreate().setOnAction(event -> {
			view.getStage().setScene(view.getCreateAccountScene());
		});

		// Szenenwechsel bei klick auf Menu change password
		view.getMenuAccountPassword().setOnAction(event -> {
			view.getStage().setScene(view.getChangePasswordScene());
		});

		// Szenenwechsel bei cancel button in Create Todo View
		view.getCreateToDoView().getBtnCancel().setOnAction(event -> {
			view.getStage().setScene(getMainScene());
		});
		// Szenenwechsel bei Cancel button von Create Account view
		view.getCreateAccountView().getBtnCancel().setOnAction(event -> {
			view.getStage().setScene(getMainScene());
		});

		// Szenenwechsel bei Cancel button von Change Password view
		view.getChangePasswordView().getBtnCancel().setOnAction(event -> {
			view.getStage().setScene(getMainScene());
		});

//		// Szenenwechsel beiSave Button in Create todo View
//		view.getCreateToDoView().getBtnSave().setOnAction(event -> {
//
//			view.getStage().setScene(getMainScene());
//		});
//
//		// Szenenwechsel bei Save button von Create Account view
//		view.getCreateAccountView().getBtnSave().setOnAction(event -> {
//
//			view.getStage().setScene(getMainScene());
//		});
//
//		// Szenenwechsel bei Save button von Change Password view
//		view.getChangePasswordView().getBtnSave().setOnAction(event -> {
//
//			view.getStage().setScene(getMainScene());
//		});

		// Wenn in app_View Button Delete geklickt wird
		view.getBtnDelete().setOnAction(event -> {
			ToDo selectedItem = view.getTableViewToDo().getSelectionModel().getSelectedItem();
			Alert alertDelete = new Alert(AlertType.CONFIRMATION);
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			alertDelete.setTitle(t.getString("alert.alertDelet.title"));
			alertDelete.setHeaderText(t.getString("alert.alertDelet.header"));

			Button okButton = (Button) alertDelete.getDialogPane().lookupButton(ButtonType.OK);
			okButton.setText(t.getString("alert.buttonOk"));
			Button cancelButton = (Button) alertDelete.getDialogPane().lookupButton(ButtonType.CANCEL);
			cancelButton.setText(t.getString("alert.buttonCancel"));

			Optional<ButtonType> result = alertDelete.showAndWait();
			if (result.get() == ButtonType.OK) {
				view.getTableViewToDo().getItems().remove(selectedItem);
				view.setStatus(t.getString("statusLabel.deletedToDo"));
			} else {

			}

		});

		// Alert für MenuItem Shortcut
		view.getMenuHelpShortcuts().setOnAction(e -> {
			Alert shortcutinfo = new Alert(AlertType.INFORMATION);

			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			shortcutinfo.setTitle(t.getString("alert.shortcutinfo.title"));
			shortcutinfo.setHeaderText(t.getString("alert.shortcutinfo.header"));
			shortcutinfo.setContentText(t.getString("alert.shortcutinfo.content"));
			shortcutinfo.showAndWait();

		});

//		// IN DEN DREI VIEWS DIE SAVE BUTTONS
//		// CreateToDoView: Save Button unter Aktion setzen
//		view.getCreateToDoView().getBtnSave().setOnAction(this::createToDo);
//		// CreateAccountView: Save Button unter Aktion setzen
//		view.getCreateAccountView().getBtnSave().setOnAction(this::createLogin);
//		// ChangePasswordView: Save Button unter Aktion setzen
//		view.getChangePasswordView().getBtnSave().setOnAction(this::changePassword);

		/**
		 * 3 ChangeListener für Textfelder Username und Password
		 */
		view.getTxtUsername().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateUsername(newValue);
		});

		view.getTxtPassword().textProperty().addListener((obserable, oldValue, newValue) -> {
			validatePassword(newValue);
		});
		view.getCreateAccountView().getTxtUsername().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateUsername(newValue);
		});

		view.getCreateAccountView().getTxtPassword().textProperty().addListener((obserable, oldValue, newValue) -> {
			validatePassword(newValue);
		});

		view.getChangePasswordView().getTxtPassword().textProperty().addListener((obserable, oldValue, newValue) -> {
			validatePassword(newValue);
		});

		/**
		 * 3 Wenn kein Zeile in der TableView angewählt ist "DEAKTIVIERE" den DELETE
		 * Button (DISABLED)
		 */
		view.getBtnDelete().disableProperty()
				.bind(Bindings.isEmpty(view.getTableViewToDo().getSelectionModel().getSelectedItems()));

		/**
		 * Buttons beim ersten Aufruf, leeren Feldern oder keiner Änderung deaktivieren
		 */
		view.getBtnLogin().setDisable(true);
		view.getBtnLogout().setDisable(true);
		// view.getBtnCreate().setDisable(true);

		view.getCreateToDoView().getBtnSave().setDisable(true);
		view.getCreateAccountView().getBtnSave().setDisable(true);
		view.getChangePasswordView().getBtnSave().setDisable(true);

		/**
		 * register ourselves to handle window-closing event
		 */
		view.getStage().setOnCloseRequest(evt -> {
			Alert closingAlert = new Alert(AlertType.CONFIRMATION);
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			closingAlert.setTitle(t.getString("alert.closingAlert.title"));
			closingAlert.setHeaderText(t.getString("alert.closingAlert.header"));

			Button okButton = (Button) closingAlert.getDialogPane().lookupButton(ButtonType.OK);
			okButton.setText(t.getString("alert.buttonOk"));
			Button cancelButton = (Button) closingAlert.getDialogPane().lookupButton(ButtonType.CANCEL);
			cancelButton.setText(t.getString("alert.buttonCancel"));

			closingAlert.showAndWait().filter(r -> r != ButtonType.OK).ifPresent(r -> evt.consume());
		});


		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application controller initialized");
	}

	protected TableView<ToDo> getTableView() {
		return view.getTableViewToDo();
	}

//3 METHODEN; UM INPUT ZU VALIDIEREN MIT CHANGELISTENER

//Für Username (Emailadresse)
	private void validateUsername(String newValue) {
		boolean valid = false; // email not ok

		// Split on '@': must give us two not-empty parts
		String[] addressParts = newValue.split("@");
		if (addressParts.length == 2 && !addressParts[0].isEmpty() && !addressParts[1].isEmpty()) {
			// We want to split the domain on '.', but split does not give us an empty
			// string, if the split-character is the last character in the string. So we
			// first ensure that the string does not end with '.'
			if (addressParts[1].charAt(addressParts[1].length() - 1) != '.') {
				// Split domain on '.': must give us at least two parts.
				// Each part must be at least two characters long
				String[] domainParts = addressParts[1].split("\\."); // Backslash hebt special characters aus
				if (domainParts.length >= 2) { // erwartet 2 teile für provider
					valid = true;

					for (String s : domainParts) {
						if (s.length() < 2)
							valid = false;
					}
				}
			}

			// Farben neutralisieren
			view.getTxtUsername().getStyleClass().remove("usernameNotOk");
			view.getTxtUsername().getStyleClass().remove("usernameok");

			view.getCreateAccountView().getTxtUsername().getStyleClass().remove("usernameNotOk");
			view.getCreateAccountView().getTxtUsername().getStyleClass().remove("usernameok");

			// Farben setzen, rot = invalid, grün = valid
			if (valid) {
				view.getTxtUsername().getStyleClass().add("usernameok");
				view.getCreateAccountView().getTxtUsername().getStyleClass().add("usernameok");

			} else {
				view.getTxtUsername().getStyleClass().add("usernameNotOk");
				view.getCreateAccountView().getTxtUsername().getStyleClass().add("usernameNotOk");
			}

			// Speichert true/false-Wert für Button aktivieren
			usernameValid = valid;
			// Aktiviert Button, wenn alles korrekt
			enableLoginButton();
			enableCreateAccountButton();
			enableChangePasswordButton();
		}
	}

	// für Password
	public void validatePassword(String newValue) {

		boolean valid = false; // password not ok
		String password = newValue;
		Translator t = ServiceLocator.getServiceLocator().getTranslator();

		if (password.length() < 3 || password.length() > 20 || password.matches("(.[0-9].)")
				|| password.matches("(.[A-Z].)") || password.matches("(.[a-z].)")) {
			valid = false;
		} else {
			valid = true;
		}
		// Farben neutralisieren
		view.getTxtPassword().getStyleClass().remove("passwordNotOk");
		view.getTxtUsername().getStyleClass().remove("passwordOk");
		view.getCreateAccountView().getTxtPassword().getStyleClass().remove("passwordNotOk");
		view.getCreateAccountView().getTxtUsername().getStyleClass().remove("passwordOk");
		view.getChangePasswordView().getTxtPassword().getStyleClass().remove("passwordNotOk");

		if (valid) {
			view.getTxtPassword().getStyleClass().add("passwordOk"); // setzt css auf grün
			view.getCreateAccountView().getTxtPassword().getStyleClass().add("passwordOk"); // setzt css auf grün
			view.getChangePasswordView().getTxtPassword().getStyleClass().add("passwordOk"); // setzt css auf grün

		} else {
			view.getTxtPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot
			view.getCreateAccountView().getTxtPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot
			view.getChangePasswordView().getTxtPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot

		}

		// Speichert true/false-Wert für Button aktivieren
		passwordValid = valid;

		// Aktiviert Button, wenn alles korrekt
		enableLoginButton();
		enableCreateAccountButton();
		enableChangePasswordButton();

	}

	private void enableLoginButton() {
		boolean valid = passwordValid & usernameValid;
		view.getBtnLogin().setDisable(!valid);
		// *********NOCH àNDERN: SetDisable bei erfolgreichem Login und Login button
		// disablen
		view.getBtnCreateToDo().setDisable(!valid);
		view.getBtnLogout().setDisable(!valid);

	}

	private void enableCreateAccountButton() {
		boolean valid = passwordValid & usernameValid;
		view.getCreateAccountView().getBtnSave().setDisable(!valid);

	}

	private void enableChangePasswordButton() {
		boolean valid = passwordValid & usernameValid;
		view.getChangePasswordView().getBtnSave().setDisable(!valid);
	}

//	// Action Events für CreateLogin, changePassword, create Todo
//	private void createLogin(ActionEvent event) {
//
//		// *************NOCH AUSFOMRULIEREN NICHT FERTIG
//
//		String username = view.getCreateAccountView().getTxtUsername().getText();
//		String password = view.getCreateAccountView().getTxtPassword().getText();
//		if (usernameValid && passwordValid) {
//
//			if (Server_ClientModel.exists(username) == null) {
//				Account account = new Account(username, password);
//				Account.add(account);
//			}
//
//			Translator t = ServiceLocator.getServiceLocator().getTranslator();
//			view.setStatus(t.getString("statusLabel.accountCreated"));
//
//		}
//
//	}

	private void changePassword(ActionEvent event) {

		// *************NOCH AUSFOMRULIEREN NICHT FERTIG
		String username = view.getChangePasswordView().getLblUsername().getText();
		String password = view.getChangePasswordView().getTxtPassword().getText();
		if (passwordValid) {

			// if (Account.exists(username) != null && Account.checkPassword(password)) {
			// Account.changePassword(password);

			// }
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.passwordChanged"));
		}
	}

	private void createToDo(ActionEvent event) {
		// ************MUSS NOCH ANGEPASST WERDEN
		String title = view.getCreateToDoView().getTxtToDo().getText();
		Priority priority = view.getCreateToDoView().getCmbPriority().getValue();
		String description = view.getCreateToDoView().getTxtDescription().getText();

		// 4. Überprüfen das Kontrollelemente nicht leer sind
		if (title != null && priority != null && description != null) {

			// 5
			model.createToDo(title, priority, description);
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.createToDo"));
			view.getCreateToDoView().reset();
		}
	}
	// ****** SZENEN WECHSEL ******

	// ****** ENDE SZENEN WECHSEL ******

	// ****** GETTER FUER DIE SZENNEN ******
	// Methode um die CreateToDo Szene aus der App_View zu holen

	// Methode um die Country Szene aus der App_View zu holen
	private Scene getMainScene() {
		return view.getMainScene();
	}
	// ****** ENDE GETTER FUER DIE SZENEN ******

	/**
	 * Methode connect - Wenn der USer auf den connect Button drückt, soll
	 * passieren: Sich der Client mit der Portnummer und
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public void connect(String ipAddress, Integer portNumber) {

		try {
			
		socket = new Socket(ipAddress, portNumber);
		inputStreamReader = new InputStreamReader(socket.getInputStream());
		bufferedReader = new BufferedReader(inputStreamReader);
		outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
		bufferedWriter = new BufferedWriter(outputStreamWriter);
		connected = true;
		System.out.println("Client hat sich verbunden mit dem Server");
		
		
		
		
		// Create thread to read incoming Message
		Runnable r = new Runnable() {
			@Override
			public void run() {
				String msg = ""; // Anything except null
				while (msg != null) { // Will be null if the server closes the socket
					
					try {
						msg = bufferedReader.readLine();
						System.out.println("Received: " + msg);

					} catch (IOException e) {
						msg = null; // end loop if we have a communications error
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();	 
		}catch(Exception e) {
			connected = false;
			e.printStackTrace();
		}
	}

	/**
	 * Wenn "Save" Button gedrückt wird mit Username & password, soll der CLient dem
	 * Server einen String mit Command|LoginUsername|password (Message_CreateLogin)
	 * senden.
	 * @throws Exception 
	 * 
	 */

	public void createLogin(String userName, String password) {
		if(connected) {
			try {
				bufferedWriter.write("CreateLogin|" + userName + "|" + password);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
				System.out.println("Sent: CreateLogin|" + userName + "|" + password);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Wenn "Login" Buttonm gedrückt wird mit Username & password einloggen
	 */
	public void login(String userName, String password) {
		if(connected) {
			try {
				bufferedWriter.write("Login|" + userName + "|" + password);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				
				System.out.println("Sent: Login|" + userName + "|" + password);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
