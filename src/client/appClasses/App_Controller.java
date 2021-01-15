package client.appClasses;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Optional;
import api.LoginHandling;
import api.ToDoHandling;
import client.ServiceLocator;
import client.abstractClasses.Controller;
import client.commonClasses.Translator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import server.Priority;
import server.ToDo;

public class App_Controller extends Controller<App_Model, App_View> implements LoginHandling, ToDoHandling {

	private String token = null;

	private Socket socket = null;
	private ArrayList<Integer> ids = new ArrayList<Integer>();

	private BufferedWriter bufferedWriter = null;
	private OutputStreamWriter outputStreamWriter = null;

	private BufferedReader bufferedReader = null;
	private InputStreamReader inputStreamReader = null;

	private boolean connected = false;
	private boolean created = false;

	ServiceLocator serviceLocator;
	Translator t = ServiceLocator.getServiceLocator().getTranslator();;

	// Speichert Wert für gültige Felder
	private boolean usernameValid = false;
	private boolean passwordValid = false;
	private boolean toDoValid = false;
	private boolean descriptionValid = false;
	private boolean priorityValid = false;

	private int lastToDo;
	private int deletedToDo;

	// Konstruktor
	public App_Controller(App_Model model, App_View view) {
		super(model, view);

		/**
		 * DONE - Connect Client with the Server Button Connect
		 */
		view.getBtnConnect().setOnAction(event -> {
			System.out.println("User klickte: 'connect'");
			connect(view.getTxtIP().getText(), Integer.parseInt(view.getTxtPort().getText()));
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.connected"));// setzt statuslabel
			view.getBtnConnect().setDisable(true); // deaktiviert button nach erfolgreichem Login

		});

		/**
		 * DONE - CreateLogin Button Save in CreateAccountView
		 */
		view.getCreateAccountView().getBtnSave().setOnAction(event -> {
			try {
				System.out.println("User klickte: 'create Account' (CreateLogin)");
				createLogin(view.getCreateAccountView().getTxtUsername().getText(),
						view.getCreateAccountView().getPwFieldPassword().getText());
				view.getStage().setScene(getMainScene());
				Translator t = ServiceLocator.getServiceLocator().getTranslator();
				view.setStatus(t.getString("statusLabel.accountCreated")); // setzt StatusLabel
				view.getCreateAccountView().reset();
				view.getCreateAccountView().getBtnSave().setDisable(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				view.setStatus(t.getString("statusLabel.accountCreatedFailed"));
			}
		});

		/**
		 * DONE - Login
		 */
		view.getBtnLogin().setOnAction(event -> {
			login(view.getTxtUsername().getText(), view.getPwFieldPassword().getText());
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.loggedIn"));// setzt status label
			enableLogoutButton();// aktiviert Logout/CreateToDo Button, wenn login erfolgreich
		});

		/**
		 * TODO - Logout
		 */
		view.getBtnLogout().setOnAction(event -> {
			// Platform.runLater(() -> {
			logout();
			System.out.println("logout ausgeführt");
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.loggedout"));// setzt status label
			// buttons wieder deaktivieren
			view.getBtnLogin().setDisable(true);
			view.getBtnLogout().setDisable(true);
			view.getBtnCreateToDo().setDisable(true);
			view.getBtnConnect().setDisable(false); // connect wird wieder aktiviert bei logout

		});

		/**
		 * DONE - CreateToDO
		 */
		view.getCreateToDoView().getBtnSave().setOnAction(event -> {

			createToDo(view.getCreateToDoView().getTxtToDo().getText(),
					view.getCreateToDoView().getCmbPriority().getValue(),
					view.getCreateToDoView().getTxtDescription().getText());

			view.getCreateToDoView().reset();

			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			view.setStatus(t.getString("statusLabel.todocreated"));// setzt statuslabel

		});
		/**
		 * DONE - DeleteToDo
		 */
		view.getBtnDelete().setOnAction(event -> {
			ToDo selectedItem = view.getTableViewToDo().getSelectionModel().getSelectedItem();
			Alert alertDelete = new Alert(AlertType.CONFIRMATION);
			Translator t = ServiceLocator.getServiceLocator().getTranslator();
			alertDelete.setTitle(t.getString("alert.alertDelete.title"));
			alertDelete.setHeaderText(t.getString("alert.alertDelete.header"));

			Button okButton = (Button) alertDelete.getDialogPane().lookupButton(ButtonType.OK);
			okButton.setText(t.getString("alert.buttonOk"));
			Button cancelButton = (Button) alertDelete.getDialogPane().lookupButton(ButtonType.CANCEL);
			cancelButton.setText(t.getString("alert.buttonCancel"));

			Optional<ButtonType> result = alertDelete.showAndWait();
			if (result.get() == ButtonType.OK) {
				view.getTableViewToDo().getItems().remove(selectedItem);
				view.getTableViewToDo().refresh();
				view.setStatus(t.getString("statusLabel.deletedToDo"));
			} else {
				// nothing
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

		/**
		 * 3 ChangeListener für Textfelder Username und Password
		 */
		view.getTxtUsername().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateUsername(newValue);
		});

		view.getPwFieldPassword().textProperty().addListener((obserable, oldValue, newValue) -> {
			validatePassword(newValue);
		});

		view.getCreateAccountView().getTxtUsername().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateUsername(newValue);
		});

		view.getCreateAccountView().getPwFieldPassword().textProperty().addListener((obserable, oldValue, newValue) -> {
			validatePassword(newValue);
		});

		view.getChangePasswordView().getPwFieldPassword().textProperty()
				.addListener((obserable, oldValue, newValue) -> {
					validatePassword(newValue);
				});

		view.getCreateToDoView().getTxtToDo().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateToDo(newValue);
		});

		view.getCreateToDoView().getTxtDescription().textProperty().addListener((obserable, oldValue, newValue) -> {
			validateDescription(newValue);
		});

		view.getCreateToDoView().getCmbPriority().getSelectionModel().selectedItemProperty()
				.addListener((obserable, oldValue, newValue) -> {
					validatePriority(newValue);
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
		view.getBtnCreateToDo().setDisable(true);

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

		/**
		 * Shortcuts
		 */
		// set shortcut ctrl'c to create to do
		view.getBtnCreateToDo().getScene().getAccelerators()
				.put(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN), new Runnable() {
					public void run() {
						view.getBtnCreateToDo().fire();
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

	// 3 METHODEN; UM INPUT ZU VALIDIEREN MIT CHANGELISTENER

	// Für Username (Emailadresse)
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
		}
		// FARBEN neutralisieren
		view.getTxtUsername().getStyleClass().remove("usernameNotOk");
		view.getTxtUsername().getStyleClass().remove("usernameok");
		view.getCreateAccountView().getTxtUsername().getStyleClass().remove("usernameNotOk");
		view.getCreateAccountView().getTxtUsername().getStyleClass().remove("usernameok");

		if (valid) {
			view.getTxtUsername().getStyleClass().add("usernameok"); // setzt css auf grün
			usernameValid = valid;
			enableLoginButton();
		} else {
			view.getTxtUsername().getStyleClass().add("usernameNotOk");// setzt css auf rot
		}
		if (valid) {
			view.getCreateAccountView().getTxtUsername().getStyleClass().add("usernameok");
			usernameValid = valid;
			enableCreateAccountButton();
		} else {
			view.getCreateAccountView().getTxtUsername().getStyleClass().add("usernameNotOk");
		}
	}

	// für Password
	public void validatePassword(String newValue) {
		boolean valid = false; // password not ok
		String password = newValue;

		if (password.length() < 3 || password.length() > 20 || !password.matches("(.*[0-9].*)")
				|| !password.matches("(.*[A-Z].*)") || !password.matches("(.*[a-z].*)")) {
			valid = false;
		} else {
			valid = true;
		}

		// Farben neutralisieren
		view.getPwFieldPassword().getStyleClass().remove("passwordNotOk");
		view.getCreateAccountView().getPwFieldPassword().getStyleClass().remove("passwordNotOk");
		view.getChangePasswordView().getPwFieldPassword().getStyleClass().remove("passwordNotOk");

		// Für App_View
		if (valid) {
			view.getPwFieldPassword().getStyleClass().add("passwordOk"); // setzt css auf grün
			passwordValid = valid;
			enableLoginButton();

		} else {
			view.getPwFieldPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot

		}
		// für CreateAccount view
		if (valid) {
			view.getCreateAccountView().getPwFieldPassword().getStyleClass().add("passwordOk"); // setzt css auf grün
			passwordValid = valid;
			enableCreateAccountButton();

		} else {
			view.getCreateAccountView().getPwFieldPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot

		}
		// für changepasswort view
		if (valid) {
			view.getChangePasswordView().getPwFieldPassword().getStyleClass().add("passwordOk"); // setzt css auf grün
			passwordValid = valid;
			enableChangePasswordButton();

		} else {
			view.getChangePasswordView().getPwFieldPassword().getStyleClass().add("passwordNotOk");// setzt css auf rot

		}

	}

	private void validateToDo(String newValue) {
		boolean valid = false;
		String toDo = newValue;

		if (toDo.length() > 0) {
			valid = true;
		}

		// Farben neutralisieren
		view.getCreateToDoView().getTxtToDo().getStyleClass().remove("inputNotOk");
		view.getCreateToDoView().getStyleClass().remove("inputOk");

		// Farben setzen, rot = invalid, grün = valid
		if (valid) {
			view.getCreateToDoView().getTxtToDo().getStyleClass().add("inputOk");
		} else {
			view.getCreateToDoView().getTxtToDo().getStyleClass().add("inputNotOk");
		}
		// Speichert true/false-Wert für Button aktivieren
		toDoValid = valid;
		// Aktiviert Button, wenn alles korrekt
		enableCreateToDoButton();
	}

	private void validateDescription(String newValue) {
		boolean valid = false;
		String description = newValue;

		if (description.length() > 0) {
			valid = true;
		}

		// Farben neutralisieren
		view.getCreateToDoView().getTxtDescription().getStyleClass().remove("inputNotOk");
		view.getCreateToDoView().getStyleClass().remove("inputOk");

		// Farben setzen, rot = invalid, grün = valid
		if (valid) {
			view.getCreateToDoView().getTxtDescription().getStyleClass().add("inputOk");
		} else {
			view.getCreateToDoView().getTxtDescription().getStyleClass().add("inputNotOk");
		}
		// Speichert true/false-Wert für Button aktivieren
		descriptionValid = valid;
		// Aktiviert Button, wenn alles korrekt
		enableCreateToDoButton();
	}

	private void validatePriority(Priority newValue) {
		boolean valid = false;
		boolean value = view.getCreateToDoView().getCmbPriority().getSelectionModel().isEmpty();

		view.getCreateToDoView().getCmbPriority().getStyleClass().remove("comboboxOk");
		view.getCreateToDoView().getCmbPriority().getStyleClass().remove("comboboxNotOk");

		if (!value) {
			valid = true;
			view.getCreateToDoView().getCmbPriority().getStyleClass().add("comboboxOk");
		}

		priorityValid = valid;
		enableCreateToDoButton();

	}

	// Bei gültigem Passwort und Username > Login Button aktivieren
	private void enableLoginButton() {
		boolean valid = usernameValid & passwordValid & created;
		view.getBtnLogin().setDisable(!valid);
	}

	// bei erfolgreichem Login > Logout und CreateToDo Button aktivieren
	private void enableLogoutButton() {
		boolean valid = true;

		view.getBtnLogin().setDisable(valid);
		view.getBtnCreateToDo().setDisable(!valid);
		view.getBtnLogout().setDisable(!valid);
	}

	// bei erfolgreichem Account erstellen > Save Button aktivieren
	private void enableCreateAccountButton() {
		boolean valid = passwordValid & usernameValid;
		view.getCreateAccountView().getBtnSave().setDisable(!valid);

	}

	// bei erfolgreichem Change Passwort > Save Button aktivieren
	private void enableChangePasswordButton() {
		boolean valid = passwordValid & usernameValid;
		view.getChangePasswordView().getBtnSave().setDisable(!valid);
	}

	// Aktiviert Button Save ToDo, wenn alle Felder != leer, Inhalt egal
	private void enableCreateToDoButton() {
		boolean valid = toDoValid & descriptionValid & priorityValid;
		view.getCreateToDoView().getBtnSave().setDisable(!valid);
	}

	// Methode um die Country Szene aus der App_View zu holen
	private Scene getMainScene() {
		return view.getMainScene();
	}

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
							// Break message into individual parts, and remove extra spaces
							String[] parts = msg.split("\\|");
							for (int i = 0; i < parts.length; i++) {
								parts[i] = parts[i].trim();
							}

							if (parts[0].equals("Result")) {

								if (parts[1].equals("Message_Login") && Boolean.parseBoolean(parts[2])) {
									token = parts[3];

								} else if (parts[1].equals("Message_CreateToDo") && Boolean.parseBoolean(parts[2])) {
									lastToDo = Integer.parseInt(parts[3]);
									getToDo(lastToDo);

								} else if (parts[1].equals("Message_GetToDo")) {
									model.getToDos().add(new ToDo(Integer.parseInt(parts[3]), parts[4],
											Priority.valueOf(parts[6]), parts[5]));
									System.out.println("Liste der ToDo's: " + model.getToDos());

								} else if (parts[1].equals("Message_ListToDos")) {
									for (int i = 3; i < parts.length; i++) {
										if (!ids.contains(Integer.parseInt(parts[i]))) {
											ids.add(Integer.parseInt(parts[i]));
										}
									}

								} else if (parts[1].equals("Message_DeleteToDo")) {
									model.getToDos().remove(deletedToDo);

								} else if (parts[1].equals("Message_Logout") && Boolean.parseBoolean(parts[2])) {
									token = null;
									Platform.runLater(() -> {
										view.stop();
										Platform.exit();
									});

									System.out.println("view.stop(); & Platform.exit(); ausgeführt");
								}
							}

						} catch (IOException e) {
							msg = null; // end loop if we have a communications error
						}
					}
				}
			};
			Thread t = new Thread(r);
			t.start();
		} catch (Exception e) {
			connected = false;
			e.printStackTrace();
		}
	}

	/**
	 * Wenn "Save" Button gedrückt wird mit Username & password, soll der CLient dem
	 * Server einen String mit Command|LoginUsername|password (Message_CreateLogin)
	 * senden.
	 * 
	 * @throws Exception
	 * 
	 */
	public void createLogin(String userName, String password) {
		if (connected) {
			try {
				bufferedWriter.write("CreateLogin|" + userName + "|" + password);
				bufferedWriter.newLine();
				bufferedWriter.flush();

				System.out.println("Sent: CreateLogin|" + userName + "|" + password);
				created = true;

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
		if (connected & created) {
			try {
				view.getChangePasswordView().setLblCurrentUsername(userName);
				bufferedWriter.write("Login|" + userName + "|" + password);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				System.out.println("Sent: Login|" + userName + "|" + password);

			} catch (Exception e) {
//				connected = false;
				e.printStackTrace();
			}
		}
	}

	public void createToDo(String title, Priority priority, String description) {
		// 4. Überprüfen das Kontrollelemente nicht leer sind

		if (title != null && priority != null && description != null && isTokenSet()) {
			try {
				bufferedWriter.write("CreateToDo|" + token + "|" + title + "|" + priority + "|" + description);
				bufferedWriter.newLine();
				bufferedWriter.flush();
				System.out.println("Sent: CreateToDo|" + token + "|" + title + "|" + priority + "|" + description);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void listToDos() {
		if (isTokenSet()) {
			try {
				bufferedWriter.write("ListToDos|" + token);
				bufferedWriter.newLine();
				bufferedWriter.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getToDo(int id) {
		if (isTokenSet()) {

			try {
				bufferedWriter.write("GetToDo|" + token + "|" + id);
				bufferedWriter.newLine();
				bufferedWriter.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	private boolean isTokenSet() {
		return (token == null) ? false : true;
	}

	@Override
	public void changePassword(String newPassword) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() {
		try {
			bufferedWriter.write("Logout");
			bufferedWriter.newLine();
			bufferedWriter.flush();

			System.out.println("Sent: Logout");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void deleteToDo(int id) {
		try {
			bufferedWriter.write("DeleteToDo|" + token + "|" + id);
			bufferedWriter.newLine();
			bufferedWriter.flush();
			deletedToDo = id;
			System.out.println("Sent: DeleteToDo|" + token + "|" + id);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void listToDos(ArrayList<Integer> ids) {
		// TODO Auto-generated method stub

	}

}