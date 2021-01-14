package client.appClasses;

import java.util.Locale;
import java.util.logging.Logger;

import client.ServiceLocator;
import client.abstractClasses.View;
import client.commonClasses.Translator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.ToDo;

public class App_View extends View<App_Model> {

	private App_Controller controller;

	// VIEW
	private CreateToDoView createToDoView;
	private CreateAccountView createAccountView;
	private ChangePasswordView changePasswordView;

	// SZENEN
	private Scene splashScene;
	private Scene mainScene; // -> App_View
	private Scene createToDoScene;
	private Scene createAccountScene;
	private Scene changePasswordScene;

	// Menu
	protected Menu menuLanguage;
	protected Menu menuAccount;
	protected MenuItem menuAccountCreate;
	protected MenuItem menuAccountPassword;
	protected Menu menuHelp;
	protected MenuItem menuHelpShortcuts;

	// Controlls
	private Label lblIP;
	private TextField txtIP;
	private Label lblPort;
	private TextField txtPort;
	private Button btnConnect;

	private Label lblUsername;
	private TextField txtUsername;
	private Label lblPassword;
	private PasswordField pwFieldPassword;
	private Button btnLogin;
	private Button btnCreateToDo;
	private Button btnDelete;
	private Button btnLogout;
	
	// 1 (Data Display) die TableView für die ToDos
	protected TableView<ToDo> tableViewToDo;
	protected TableColumn<ToDo, Integer> colID;
	protected TableColumn<ToDo, String> colToDo;
	protected TableColumn<ToDo, String> colDescription;
	protected TableColumn<ToDo, String> colPriority;

	// Aktueller Status Label
	private Label lblStatus;

	// 0 Konstruktor
	public App_View(Stage stage, App_Model model) {
		super(stage, model);

		ServiceLocator.getServiceLocator().getLogger().info("Application view initialized");
		mainScene = create_GUI();

		stage.setScene(mainScene);
		stage.setResizable(true);
		mainScene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());

	}

	public App_Model getModel() {
		return model;
	}

	public void setModel(App_Model model) {
		this.model = model;
	}

	public Scene getMainScene() {
		return mainScene;
	}

	public void setMainScene(Scene mainScene) {
		this.mainScene = mainScene;
	}

	public void start() {
		stage.show();
	}

	public void stop() {
		stage.hide();

	}

	public Pane createControlPaneStatus() {
		BorderPane statusBox = new BorderPane();
		statusBox.setId("StatusBox");
		this.lblStatus = new Label("");
		this.lblStatus.getStyleClass().add("statusLabel");

		statusBox.setCenter(this.lblStatus);
		BorderPane.setAlignment(lblStatus, Pos.CENTER);
		BorderPane.setMargin(lblStatus, new Insets(12,12,12,12)); 
		return statusBox;
		}
	
	// Methode um die Kontrollelemente zu erzeugen (Login)
	public Pane createControlPaneLogin() {

		GridPane topBox = new GridPane();
		topBox.setId("TopBox");

		lblIP = new Label("IP");
		txtIP = new TextField("127.0.0.1");
		lblPort = new Label("Port");
		txtPort = new TextField("50002");
		btnConnect = new Button("Connect");
		lblUsername = new Label("Username");
		txtUsername = new TextField("");
		lblPassword = new Label("Password");
		pwFieldPassword = new PasswordField();
		btnLogin = new Button("Login");

		topBox.add(lblIP, 0, 0);
		topBox.add(txtIP, 2, 0);
		topBox.add(lblPort, 4, 0);
		topBox.add(txtPort, 6, 0);
		topBox.add(btnConnect, 8, 0);
		topBox.add(lblUsername, 10, 0);
		topBox.add(txtUsername, 12, 0);
		topBox.add(lblPassword, 14, 0);
		topBox.add(pwFieldPassword, 16, 0);
		topBox.add(btnLogin, 18, 0);
        
		topBox.setHgap(5);
		topBox.setPadding(new Insets(10, 10, 10, 10));

		return topBox;
	}

	// Methode um die Kontrollelemente zu erzeugen (To Do verwalten)
	public Pane createControlPaneToDo() {
		GridPane bottomBox = new GridPane();
		bottomBox.setId("BottomBox");

		btnCreateToDo = new Button("Create ToDo");
		btnDelete = new Button("Delete ToDo");
		btnLogout = new Button("Logout");

		bottomBox.add(btnCreateToDo, 0, 0);
		bottomBox.add(btnDelete, 2, 0);
		bottomBox.add(btnLogout, 4, 0);
		bottomBox.setHgap(5);
		bottomBox.setPadding(new Insets(10, 10, 10, 10));
		return bottomBox;
	}

	/*
	 * Data Display Pane TableView für die "todo" Liste
	 */
	private TableView<ToDo> createTableViewToDo() {
		this.tableViewToDo = new TableView<ToDo>();
		this.tableViewToDo.setEditable(false);
		this.tableViewToDo.setPlaceholder(new Label("-"));

		// Each column needs a title, and a source of data.
		// For editable columns, each column needs to contain a TextField.

		// ID Spalte
		colID = new TableColumn<>("ID"); // Erstellen und Beschriftung der Spalte
		colID.setMinWidth(50);
		colID.setCellValueFactory(new PropertyValueFactory<>("id")); // Insatnzieren ein Property und übergeben
		tableViewToDo.getColumns().add(colID); // Fügen der TableView die Spalte hinzu

		// Title Spalte
		colToDo = new TableColumn<>("Title");
		colToDo.setMinWidth(300);
		colToDo.setCellValueFactory(new PropertyValueFactory<>("title"));
		tableViewToDo.getColumns().add(colToDo);

		// Description Spalte
		colDescription = new TableColumn<>("Description");
		colDescription.setMinWidth(600);
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		tableViewToDo.getColumns().add(colDescription);

		// Priority Spalte
		colPriority = new TableColumn<>("Priority");
		colPriority.setMinWidth(150);
		colPriority.setCellValueFactory(new PropertyValueFactory<ToDo, String>("priority"));
		tableViewToDo.getColumns().add(colPriority);

		// Finally, attach the tableView to the ObservableList of data
		tableViewToDo.setItems(model.getToDos());

		return tableViewToDo;
	}

	// Methode die Create, ChangePasswort, Account View zu erzeugen
	public Pane createToDoView() {
		Pane pane = new Pane();
		this.setCreateToDoView(new CreateToDoView(stage, model, controller));
		pane.getChildren().add(this.getCreateToDoView());
		return pane;

	}

	public Pane changePasswordView() {
		Pane pane = new Pane();
		this.setChangePasswordView(new ChangePasswordView(stage, model, controller));
		pane.getChildren().add(this.getChangePasswordView());
		return pane;

	}

	public Pane createAccountView() {
		Pane pane = new Pane();
		this.setCreateAccountView(new CreateAccountView(stage, model, controller));
		pane.getChildren().add(this.getCreateAccountView());
		return pane;

	}

	// Methode um den Status zu aktualiseren
	public void setStatus(String message) {
		this.lblStatus.setText(message); // status = Label
	}

	// Getter & Setter Tabelview
	public TableView<ToDo> getTableViewToDo() {
		return this.tableViewToDo;
	}

	public void setTableViewToDo(TableView<ToDo> tableViewToDo) {
		this.tableViewToDo = tableViewToDo;
	}

	// Getter & Setter ID
	public TableColumn<ToDo, Integer> getColID() {
		return colID;
	}

	public void setColID(TableColumn<ToDo, Integer> colID) {
		this.colID = colID;
	}

	// Getter & Setter ToDo
	public TableColumn<ToDo, String> getColToDo() {
		return colToDo;
	}

	public void setColToDo(TableColumn<ToDo, String> colToDo) {
		this.colToDo = colToDo;
	}

	// Getter & Setter Description
	public TableColumn<ToDo, String> getColDescription() {
		return colDescription;
	}

	public void setColDescription(TableColumn<ToDo, String> colDescription) {
		this.colDescription = colDescription;
	}

	// Getter & Setter Priority
	public TableColumn<ToDo, String> getColPriority() {
		return colPriority;
	}

	public void setColPriority(TableColumn<ToDo, String> colPriority) {
		this.colPriority = colPriority;
	}


	// Getter & Setter für Menu
	public MenuItem getMenuHelpShortcuts() {
		return menuHelpShortcuts;
	}

	public void setMenuHelpShortcuts(MenuItem menuHelpShortcuts) {
		this.menuHelpShortcuts = menuHelpShortcuts;
	}

	public MenuItem getMenuAccountCreate() {
		return menuAccountCreate;
	}

	public void setMenuAccountCreate(MenuItem menuAccountCreate) {
		this.menuAccountCreate = menuAccountCreate;
	}

	public MenuItem getMenuAccountPassword() {
		return menuAccountPassword;
	}

	public void setMenuAccountPassword(MenuItem menuAccountPassword) {
		this.menuAccountPassword = menuAccountPassword;
	}

	// Getter & Setter für Buttons
	public Button getBtnCreateToDo() {
		return btnCreateToDo;
	}

	public void setBtnCreateToDo(Button btnCreateToDo) {
		this.btnCreateToDo = btnCreateToDo;
	}

	public Button getBtnDelete() {
		return btnDelete;
	}

	public void setBtnDelete(Button btnDelete) {
		this.btnDelete = btnDelete;
	}

	public Button getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(Button btnLogin) {
		this.btnLogin = btnLogin;
	}

	public Button getBtnConnect() {
		return btnConnect;
	}

	public void setBtnConnect(Button btnConnect) {
		this.btnConnect = btnConnect;
	}

	public Button getBtnLogout() {
		return btnLogout;
	}

	public void setBtnLogout(Button btnLogout) {
		this.btnLogout = btnLogout;
	}

	// Getter & Setter für Textfelder
	public TextField getTxtIP() {
		return txtIP;
	}

	public void setTxtIP(TextField txtIP) {
		this.txtIP = txtIP;
	}

	public TextField getTxtPort() {
		return txtPort;
	}

	public void setTxtPort(TextField txtPort) {
		this.txtPort = txtPort;
	}

	public TextField getTxtUsername() {
		return txtUsername;
	}

	public void setTxtUsername(TextField txtUsername) {
		this.txtUsername = txtUsername;
	}

	public PasswordField getPwFieldPassword() {
		return pwFieldPassword;
	}

	public void setPwFieldPassword(PasswordField pwFieldPassword) {
		this.pwFieldPassword = pwFieldPassword;
	}

	// Getter & Setter für Stage & Controller
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public App_Controller getController() {
		return controller;
	}

	public void setController(App_Controller controller) {
		this.controller = controller;
	}

	// ***** VIEW *****
	// Getter & Setter für die views
	public CreateToDoView getCreateToDoView() {
		return this.createToDoView;
	}

	public void setCreateToDoView(CreateToDoView createToDoView) {
		this.createToDoView = createToDoView;
	}

	public CreateAccountView getCreateAccountView() {
		return this.createAccountView;
	}

	public void setCreateAccountView(CreateAccountView createAccountView) {
		this.createAccountView = createAccountView;
	}

	public ChangePasswordView getChangePasswordView() {
		return this.changePasswordView;
	}

	public void setChangePasswordView(ChangePasswordView changePasswordView) {
		this.changePasswordView = changePasswordView;
	}

	// ***** SZENEN ***** (GETTER & SETTER)
	public Scene getCreateToDoScene() {
		return createToDoScene;
	}

	public void setCreateToDoScene(Scene createToDoScene) {
		this.createToDoScene = createToDoScene;
	}

	public Scene getCreateAccountScene() {
		return createAccountScene;
	}

	public void setCreateAccountScene(Scene createAccountScene) {
		this.createAccountScene = createAccountScene;
	}

	public Scene getChangePasswordScene() {
		return changePasswordScene;
	}

	public void setChangePasswordScene(Scene changePasswordScene) {
		this.changePasswordScene = changePasswordScene;
	}

	public Scene getSplashScene() {
		return splashScene;
	}

	public void setSplashScene(Scene splashScene) {
		this.splashScene = splashScene;
	}

	// ***** ENDE SZENEN *****

	protected Scene create_GUI() {
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Logger logger = sl.getLogger();

		MenuBar menuBar = new MenuBar();
		menuLanguage = new Menu();

		for (Locale locale : sl.getLocales()) {
			MenuItem language = new MenuItem(locale.getLanguage());
			menuLanguage.getItems().add(language);
			language.setOnAction(event -> {
				sl.getConfiguration().setLocalOption("Language", locale.getLanguage());
				sl.setTranslator(new Translator(locale.getLanguage()));
				updateTexts();
			});
		}

		menuHelp = new Menu();
		menuHelpShortcuts = new MenuItem("Shortcuts");
		menuHelp.getItems().add(menuHelpShortcuts);

		menuAccount = new Menu();
		menuAccountCreate = new MenuItem("Account erstellen");
		menuAccountPassword = new MenuItem("Passwort ändern");
		menuAccount.getItems().addAll(menuAccountCreate, menuAccountPassword);

		menuBar.getMenus().addAll(menuAccount, menuLanguage, menuHelp);

		GridPane root = new GridPane();
		root.add(menuBar, 0, 0);

		root.add(createControlPaneLogin(), 0, 1);

		root.add(createControlPaneStatus(), 0, 2);
		
		this.tableViewToDo = createTableViewToDo();
		root.add(tableViewToDo, 0, 3);
		this.tableViewToDo.prefWidthProperty().bind(root.widthProperty());

		root.add(createControlPaneToDo(), 0, 4);

		// SZENEN
		createToDoScene = new Scene(createToDoView(), 450, 450);
		createAccountScene = new Scene(createAccountView(), 450, 450);
		changePasswordScene = new Scene(changePasswordView(), 450, 450);

		updateTexts();

		Scene scene = new Scene(root);
		return scene;
	}

	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();

		// The menu entries
		menuLanguage.setText(t.getString("program.menu.language"));
		menuHelp.setText(t.getString("program.menu.help"));
		menuHelpShortcuts.setText(t.getString("program.menu.help.shortcuts"));
		menuAccount.setText(t.getString("program.menu.account"));
		menuAccountCreate.setText(t.getString("program.menu.accountCreate"));
		menuAccountPassword.setText(t.getString("program.menu.accountPassword"));

		// controls
		lblIP.setText(t.getString("lbl.IP"));
		lblPort.setText(t.getString("lbl.port"));
		lblUsername.setText(t.getString("lbl.user"));
		lblPassword.setText(t.getString("lbl.password"));

		btnLogin.setText(t.getString("button.login"));
		btnCreateToDo.setText(t.getString("button.create"));
		btnDelete.setText(t.getString("button.delete"));
		btnLogout.setText(t.getString("button.logout"));
		btnConnect.setText(t.getString("button.connect"));

		lblStatus.setText(t.getString("statusLabel.beginning"));

		stage.setTitle(t.getString("program.name"));

		// Labels CreateToDoView
		getCreateToDoView().getLblTitle().setText(t.getString("createToDoView.label.title"));
		getCreateToDoView().getLblToDo().setText(t.getString("createToDoView.label.toDo"));
		getCreateToDoView().getLblPriority().setText(t.getString("createToDoView.label.priority"));

		// Buttons CreateToDoView
		getCreateToDoView().getBtnSave().setText(t.getString("createToDoView.button.save"));
		getCreateToDoView().getBtnCancel().setText(t.getString("createToDoView.button.cancel"));

		// Labels CreateAccountView
		getCreateAccountView().getLblTitle().setText(t.getString("createAccountView.label.title"));
		getCreateAccountView().getLblUsername().setText(t.getString("createToDoView.label.username"));
		getCreateAccountView().getLblPassword().setText(t.getString("createAccountView.label.password"));
		getCreateAccountView().getLblInfo().setText(t.getString("createAccountView.label.info"));

		// Buttons CreateAccountView
		getCreateAccountView().getBtnSave().setText(t.getString("createAccountView.button.save"));
		getCreateAccountView().getBtnCancel().setText(t.getString("createAccountView.button.cancel"));

		// Labels ChangePasswordView
		getChangePasswordView().getLblTitle().setText(t.getString("changePasswordView.label.title"));
		getChangePasswordView().getLblUsername().setText(t.getString("changePasswordView.label.username"));
		getChangePasswordView().getLblPassword().setText(t.getString("changePasswordView.label.password"));
		getChangePasswordView().getLblInfo().setText(t.getString("changePasswordView.label.info"));

		// Buttons ChangePasswordView
		getChangePasswordView().getBtnSave().setText(t.getString("changePasswordView.button.save"));
		getChangePasswordView().getBtnCancel().setText(t.getString("changePasswordView.button.cancel"));

		// TabelView dependency
		getTableViewToDo().getColumns().get(0).setText(t.getString("row.Tableview.ID"));
		getTableViewToDo().getColumns().get(1).setText(t.getString("row.Tableview.ToDo"));
		getTableViewToDo().getColumns().get(2).setText(t.getString("row.Tableview.description"));
		getTableViewToDo().getColumns().get(3).setText(t.getString("row.Tableview.priority"));

	}

}