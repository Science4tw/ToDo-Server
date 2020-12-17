package client.appClasses;

import java.util.Optional;

import client.ServiceLocator;
import client.abstractClasses.Controller;
import client.commonClasses.Translator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.WindowEvent;
import server.Priority;

public class App_Controller extends Controller<App_Model, App_View> {
	public App_View view;
	public App_Model model;

	ServiceLocator serviceLocator;

	private final static int MIN_LENGTH = 3;
	private final static int MAX_LENGTH = 20;

	private boolean usernameValid = false;
	private boolean passwordValid = false;

	// Konstruktor
	public App_Controller(App_Model model, App_View view) {
		super(model, view);
	
		// *** SZENEN WECHSEL ***
		// Wenn in der App_View der Create Button gedrückt wird,
		// soll die Sczene gewechselt werden zu der CreateView
		view.getBtnCreate().setOnAction(event -> {
			view.getStage().setScene(view.getCreateScene());
		});

		// Wenn in der CreateView der Cancel Button gedrückt wird,
		// soll die Sczene gewechselt werden
		view.getCreateView().getBtnCancel().setOnAction(event -> {
			view.getCreateView().reset();
			view.getStage().setScene(getMainScene());
		});

		// Wenn in der CreateView der Save Button gedrückt wird,
		// soll die Sczene gewechselt werden
		view.getCreateView().getBtnSave().setOnAction(event -> {
			view.getCreateView().reset();
			view.getStage().setScene(getMainScene());
		});

		// Wenn in View Button Delete geklickt wird
		view.getBtnDelete().setOnAction(event -> {
			ToDo selectedItem = view.getTableView().getSelectionModel().getSelectedItem();
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
				view.getTableView().getItems().remove(selectedItem);
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


		// CreateView: Save Button unter Aktion setzen
		view.getCreateView().getBtnSave().setOnAction(this::saveToDo);

		/**
		 * 3 ChangeListener für Textfelder Username und Password
		 */
		view.getTxtUsername().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateUsername(newValue);
		});

		view.getTxtPassword().textProperty().addListener((obserable, oldValue, newValue) -> {
			validatePassword(newValue);
		});

		/**
		 * 3 Wenn kein Zeile in der TableView angewählt ist "DEAKTIVIERE" den DELETE
		 * Button (DISABLED)
		 */
		view.getBtnDelete().disableProperty()
				.bind(Bindings.isEmpty(view.getTableView().getSelectionModel().getSelectedItems()));

		/**
		 * Buttons beim ersten Aufruf, leeren Feldern oder keiner Änderung deaktivieren
		 */
		view.getBtnLogin().setDisable(true);
		view.getBtnLogout().setDisable(true);
		//view.getBtnCreate().setDisable(true);

		view.getCreateView().getBtnSave().setDisable(true);

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
		// set shortcut ctrl's to save to do
		view.getCreateView().getBtnSave().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getCreateView().getBtnSave().fire();
					}
				});
		// set shortcut ctrl'c to cancel
		view.getCreateView().getBtnCancel().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getCreateView().getBtnCancel().fire();
					}
				});

		serviceLocator = ServiceLocator.getServiceLocator();
		serviceLocator.getLogger().info("Application controller initialized");
	}

	protected TableView<ToDo> getTableView() {
		return view.getTableView();
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

		// Farben setzen, rot = invalid, grün = valid
		if (valid) {
			view.getTxtUsername().getStyleClass().add("usernameok");
		} else {
			view.getTxtUsername().getStyleClass().add("usernameNotOk");
		}

		// Speichert true/false-Wert für Button aktivieren
		usernameValid = valid;
		// Aktiviert Button, wenn alles korrekt
		enableLoginButton();
	}
	}
	// für Password
	public void validatePassword(String newValue) {

		boolean valid = false; // password not ok
		String password = newValue;
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		
		if (password.length() < MIN_LENGTH) {
			view.setStatus(t.getString("statusLabel.passwordtooshort"));
			valid = false;
		} else if (password.length() < MAX_LENGTH) {
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

	if(valid){
		view.getTxtPassword().getStyleClass().add("passwordOk"); // setzt css auf grün
	}else{
		view.getTxtPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot
	}
	
	// Speichert true/false-Wert für Button aktivieren
	passwordValid=valid;

	// Aktiviert Button, wenn alles korrekt
	enableLoginButton();

	}
	
	
	private void enableLoginButton() {
		boolean valid = passwordValid & usernameValid;
		view.getBtnLogin().setDisable(!valid);
		//*********NOCH àNDERN: SetDisable bei erfolgreichem Login und Login button disablen
		view.getBtnCreate().setDisable(!valid);
		view.getBtnLogout().setDisable(!valid);

		

}

	private void newToDo(ActionEvent event) {
		
	}

	private void saveToDo(ActionEvent event) {
		int ID = 1; //************MUSS NOCH ANGEPASST WERDEN
		String todo = view.getCreateView().getTxtToDo().getText();
		String description = view.getCreateView().getDescription().getText();
		Priority priority = view.getCreateView().getCmbPriority().getValue();
		String duedate = view.getCreateView().getTxtDueDate().getText();
		
		// 4. Überprüfen das Kontrollelemente nicht leer sind
		if (todo != null && description != null && priority != null && duedate != null) {

		// 5
		model.createToDo(ID, todo, description, priority, duedate);
		Translator t = ServiceLocator.getServiceLocator().getTranslator();
		view.setStatus(t.getString("statusLabel.createToDo"));
		view.getCreateView().reset(); 
		}
	}
	// ****** SZENEN WECHSEL ******

	// ****** ENDE SZENEN WECHSEL ******

	// ****** GETTER FUER DIE SZENNEN ******
	// Methode um die Country Szene aus der App_View zu holen
	private Scene getCreateScene() {
		return view.getCreateScene();

	}
	// Methode um die Country Szene aus der App_View zu holen
		private Scene getMainScene() {
			return view.getMainScene();
		}
	// ****** ENDE GETTER FUER DIE SZENEN ******

	


}
