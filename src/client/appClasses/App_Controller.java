package client.appClasses;

import java.util.Optional;
import client.ServiceLocator;
import client.abstractClasses.Controller;
import client.commonClasses.Translator;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import server.Priority;
import server.ToDo;

public class App_Controller extends Controller<App_Model, App_View> {

	ServiceLocator serviceLocator;
	
	// Speichert Wert für gültige Textfelder
	private boolean usernameValid = false;
	private boolean passwordValid = false;

	// Konstruktor
	public App_Controller(App_Model model, App_View view) {
		super(model, view);

		// *** SZENEN WECHSEL ***
		// Wenn in der App_View der Create Button gedrückt wird,
		// soll die Sczene gewechselt werden zu der CreateView
		view.getBtnCreate().setOnAction(event -> {
			view.getStage().setScene(view.getCreateToDoScene());
		});
		// Szenenwechsel bei Menu create account
		view.getMenuAccountCreate().setOnAction(event -> {
			view.getStage().setScene(view.getCreateAccountScene());
		});

		// Szenenwechsel bei Menu change password
		view.getMenuAccountPassword().setOnAction(event -> {
			view.getStage().setScene(view.getChangePasswordScene());
		});

		// Wenn in der CreateView der Cancel Button gedrückt wird,
		// soll die Sczene gewechselt werden
		view.getCreateToDoView().getBtnCancel().setOnAction(event -> {
			view.getCreateToDoView().reset();
			view.getStage().setScene(getMainScene());
		});
		// Szenenwechsel bei Cancel button von Create Account view
		view.getCreateAccountView().getBtnCancel().setOnAction(event -> {
			view.getCreateAccountView().reset();
			view.getStage().setScene(getMainScene());
		});

		// Szenenwechsel bei Cancel button von Change Password view
		view.getChangePasswordView().getBtnCancel().setOnAction(event -> {
			view.getChangePasswordView().reset();
			view.getStage().setScene(getMainScene());
		});

		// Wenn in der CreateView der Save Button gedrückt wird,
		// soll die Sczene gewechselt werden
		view.getCreateToDoView().getBtnSave().setOnAction(event -> {
			view.getCreateToDoView().reset();
			view.getStage().setScene(getMainScene());
		});

		// Szenenwechsel bei Save button von Create Account view
		view.getCreateAccountView().getBtnSave().setOnAction(event -> {
			view.getCreateAccountView().reset();
			view.getStage().setScene(getMainScene());
		});

		// Szenenwechsel bei Save button von Change Password view
		view.getChangePasswordView().getBtnSave().setOnAction(event -> {
			view.getChangePasswordView().reset();
			view.getStage().setScene(getMainScene());
		});

		// ********NOCH NICHT FERTIG
		// Wenn in View Button Connect geklickt wird
		view.getBtnConnect().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				
				// *****
				Translator t = ServiceLocator.getServiceLocator().getTranslator();
				view.setStatus(t.getString("statusLabel.initialized"));
			}
		});
		// ********NOCH NICHT FERTIG
		// Wenn in View Button Login geklickt wird
		view.getBtnLogin().setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				// *****
					Translator t = ServiceLocator.getServiceLocator().getTranslator();
				view.setStatus(t.getString("statusLabel.loggedIn"));
			}
		});

		// Wenn in View Button Delete geklickt wird
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

		// IN DEN DREI VIEWS DIE SAVE BUTTONS
		// CreateToDoView: Save Button unter Aktion setzen
		view.getCreateToDoView().getBtnSave().setOnAction(this::saveToDo);
		// CreateAccountView: Save Button unter Aktion setzen
		view.getCreateAccountView().getBtnSave().setOnAction(this::saveAccount);
		// ChangePasswordView: Save Button unter Aktion setzen
		view.getChangePasswordView().getBtnSave().setOnAction(this::savePassword);

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

		// register ourselves to handle window-closing event
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

		/*
		 * Shortcuts
		 */
		// set shortcut ctrl'c to create to do
		view.getBtnCreate().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getBtnCreate().fire();
					}
				});

		// set shortcut ctrl'd to delete to do
		view.getBtnDelete().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getBtnDelete().fire();
					}
				});
		// set shortcut ctrl's to save ToDo, Password, Account
		view.getCreateToDoView().getBtnSave().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getCreateToDoView().getBtnSave().fire();
					}
				});
		view.getCreateAccountView().getBtnSave().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getCreateAccountView().getBtnSave().fire();
					}
				});
		view.getChangePasswordView().getBtnSave().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getChangePasswordView().getBtnSave().fire();
					}
				});
		// set shortcut ctrl'c to cancel ToDo, Password, Account
		view.getCreateToDoView().getBtnCancel().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getCreateToDoView().getBtnCancel().fire();
					}
				});
		view.getCreateAccountView().getBtnCancel().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getCreateAccountView().getBtnCancel().fire();
					}
				});
		view.getChangePasswordView().getBtnCancel().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getChangePasswordView().getBtnCancel().fire();
					}
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
			enableSaveAccountButton();
			enableSavePasswordButton();
		}
	}

	// für Password
	public void validatePassword(String newValue) {

		boolean valid = false; // password not ok
		String password = newValue;
		Translator t = ServiceLocator.getServiceLocator().getTranslator();

		if (password.length() < 3) {
			view.setStatus(t.getString("statusLabel.passwordtooshort"));
			valid = false;
		} else if (password.length() < 20) {
			view.setStatus(t.getString("statusLabel.passwordtoolong"));
			valid = false;
		} else if (!password.matches(".*\\d.*")) {
			view.setStatus(t.getString("statusLabel.passwortdigit"));
			valid = false;
		} else if (!password.matches(".*[A-Z].*")) {
			view.setStatus(t.getString("statusLabel.passworduppercase"));
			valid = false;
		} else if (!password.matches(".*[a-z].*")) {
			view.setStatus(t.getString("statusLabel.passwordlowercase"));
			valid = false;
		}
		valid = true;

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
		enableSaveAccountButton();
		enableSavePasswordButton();

	}

	private void enableLoginButton() {
		boolean valid = passwordValid & usernameValid;
		view.getBtnLogin().setDisable(!valid);
		// *********NOCH àNDERN: SetDisable bei erfolgreichem Login und Login button
		// disablen
		view.getBtnCreate().setDisable(!valid);
		view.getBtnLogout().setDisable(!valid);

	}

	private void enableSaveAccountButton() {
		boolean valid = passwordValid & usernameValid;
		view.getCreateAccountView().getBtnSave().setDisable(!valid);

	}

	private void enableSavePasswordButton() {
		boolean valid = passwordValid & usernameValid;
		view.getChangePasswordView().getBtnSave().setDisable(!valid);
	}

	private void saveAccount(ActionEvent event) {
		if (usernameValid && passwordValid) {
			// *************NOCH AUSFOMRULIEREN NICHT FERTIG

			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.accountCreated"));

		}

	}

	private void savePassword(ActionEvent event) {
		if (passwordValid) {
			//*************NOCH AUSFOMRULIEREN NICHT FERTIG
		
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		view.setStatus(t.getString("statusLabel.passwordChanged"));
		}
	}

	private void saveToDo(ActionEvent event) {
		int ID = 1; // ************MUSS NOCH ANGEPASST WERDEN
		String todo = view.getCreateToDoView().getTxtToDo().getText();
		Priority priority = view.getCreateToDoView().getCmbPriority().getValue();
		String duedate = view.getCreateToDoView().getTxtDueDate().getText();

		// 4. Überprüfen das Kontrollelemente nicht leer sind
		if (todo != null && priority != null && duedate != null) {

			// 5
			model.createToDo(todo, priority, duedate);
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

}
