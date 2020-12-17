package client.appClasses;

import java.util.Locale;
import java.util.logging.Logger;

import client.ServiceLocator;
import client.abstractClasses.View;
import client.commonClasses.Translator;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class App_View extends View<App_Model> {

	private App_Controller controller;

	// VIEW
	private CreateView createView;

	// SZENEN
	private Scene splashScene;
	private Scene mainScene; // -> App_View 
	private Scene createScene;
		
	
	//Menu
	protected Menu menuFile;
	protected Menu menuFileLanguage;
	protected Menu menuHelp;
	protected MenuItem menuHelpShortcuts;

	
	//Controlls
	private Label lblIP;
	private TextField txtIP;
	private Label lblPort;
	private TextField txtPort;
	private Label lblUsername;
	private TextField txtUsername;
	private Label lblPassword;
	private TextField txtPassword;
	private Button btnLogin;
	private Button btnCreate;
	private Button btnDelete;
	private Button btnLogout;
	
	// 1 (Data Display) die TableView für die ToDos
	protected TableView<ToDo> tableView;
	protected TableColumn<ToDo, Integer> colID;
	protected TableColumn<ToDo, String> colToDo;
	protected TableColumn<ToDo, String> colDescription;
	protected TableColumn<ToDo, String> colPriority;
	protected TableColumn<ToDo, String> colDueDate;

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
	
	public Scene getCreateScene() {
		return this.createScene;
	}

	public void setCreateScene(Scene createScene) {
		this.createScene = createScene;
	}
	public Scene getSplashScene() {
		return splashScene;
	}

	public void setSplashScene(Scene splashScene) {
		this.splashScene = splashScene;
	}

	public void start() {
		stage.show();
	}

	public void stop() {
		stage.hide();

	}

	// Methode um die Kontrollelemente zu erzeugen (Login)
	public Pane controlPaneLogin() {
		
		GridPane topBox = new GridPane();
		topBox.setId("TopBox");
	
		lblIP = new Label("IP");
	 	txtIP = new TextField("");
	 	lblPort = new Label("Port");
	 	txtPort = new TextField("");
	 	lblUsername = new Label("Username");
	 	txtUsername = new TextField("");
	 	lblPassword = new Label("Password");
	 	txtPassword = new TextField("");
	 	btnLogin = new Button("Login");
	 
	 	topBox.add(lblIP, 0, 0);
		topBox.add(txtIP, 2, 0);
		topBox.add(lblPort, 4, 0);
		topBox.add(txtPort, 6, 0);
		topBox.add(lblUsername, 8, 0);
		topBox.add(txtUsername, 10, 0);
		topBox.add(lblPassword, 12, 0);
		topBox.add(txtPassword, 14, 0);
		topBox.add(btnLogin, 16, 0);
		topBox.setHgap(5);
		topBox.setPadding(new Insets(10, 10, 10, 10));
		
		return topBox;
	}
	
	// Methode um die Kontrollelemente zu erzeugen (To Do verwalten)
		public Pane controlPaneToDo() {
			GridPane bottomBox = new GridPane();
			bottomBox.setId("BottomBox");
			
			btnCreate = new Button("Create To Do");
		 	btnDelete = new Button("Delete To Do");
		 	btnLogout = new Button("Logout");
		 	
		 	bottomBox.add(btnCreate, 0, 0);
			bottomBox.add(btnDelete, 2, 0);
			bottomBox.add(btnLogout, 4, 0);
			bottomBox.setHgap(5);
			bottomBox.setPadding(new Insets(10, 10, 10, 10));
			return bottomBox;
		}
	
	/*
	 * Data Display Pane TableView für die "todo" Liste
	 */
	private TableView<ToDo> createTableView() {
		this.tableView = new TableView<ToDo>();
		this.tableView.setEditable(false);
		this.tableView.setPlaceholder(new Label("-"));

		// Each column needs a title, and a source of data.
		// For editable columns, each column needs to contain a TextField.

		// ID Spalte
		colID = new TableColumn<>("ID"); // Erstellen und Beschriftung der Spalte
		colID.setMinWidth(200);
		colID.setCellValueFactory(new PropertyValueFactory<>("id")); // Insatnzieren ein Property und übergeben
		tableView.getColumns().add(colID); // Fügen der TableView die Spalte hinzu

		// Title Spalte
		colToDo = new TableColumn<>("To Do");
		colToDo.setMinWidth(200);
		colToDo.setCellValueFactory(new PropertyValueFactory<>("To Do"));
		tableView.getColumns().add(colToDo);

		// Description Spalte
		colDescription = new TableColumn<>("Description");
		colDescription.setMinWidth(200);
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		tableView.getColumns().add(colDescription);

		// Priority Spalte
		colPriority = new TableColumn<>("Priority");
		colPriority.setMinWidth(200);
		colPriority.setCellValueFactory(new PropertyValueFactory<ToDo, String>("priority"));
		tableView.getColumns().add(colPriority);

		// DueDate Spalte
		colDueDate = new TableColumn<>("DueDate");
		colDueDate.setMinWidth(200);
		colDueDate.setCellValueFactory(new PropertyValueFactory<ToDo, String>("dueDate"));
		tableView.getColumns().add(colDueDate);
				
		// Finally, attach the tableView to the ObservableList of data
		tableView.setItems(model.getToDos());

		return tableView;
	}
	
	// Methode die Create View zu erzeugen
		public Pane createView() {
			Pane pane = new Pane();
			this.setCreateView(new CreateView(stage, model, controller));
			pane.getChildren().add(this.getCreateView());
			return pane;

		}
		
		// Methode um den Status zu aktualiseren
		public void setStatus(String message) {
			this.lblStatus.setText(message); // status = Label
		}
		
		// Getter & Setter Tabelview
		public TableView<ToDo> getTableView() {
			return this.tableView;
		}
		public void setTableView(TableView<ToDo> tableView) {
			this.tableView = tableView;
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

		// Getter & Setter DueDate
		public TableColumn<ToDo, String> getColDueDate() {
			return colDueDate;
		}

		public void setColDueDate(TableColumn<ToDo, String> colDueDate) {
			this.colDueDate = colDueDate;
		}

		//Getter & Setter für Menu
		public MenuItem getMenuHelpShortcuts() {
			return menuHelpShortcuts;
		}

		public void setMenuHelpShortcuts(MenuItem menuHelpShortcuts) {
			this.menuHelpShortcuts = menuHelpShortcuts;
		}

		//Getter & Setter für Buttons
		public Button getBtnCreate() {
			return btnCreate;
		}

		public void setBtnCreate(Button btnCreate) {
			this.btnCreate = btnCreate;
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
		public Button getBtnLogout() {
			return btnLogout;
		}

		public void setBtnLogout(Button btnLogout) {
			this.btnLogout = btnLogout;
		}
		
		//Getter & Setter für Textfelder Username/Passwort
		public TextField getTxtUsername() {
			return txtUsername;
		}

		public void setTxtUsername(TextField txtUsername) {
			this.txtUsername = txtUsername;
		}
		public TextField getTxtPassword() {
			return txtPassword;
		}

		public void setTxtPassword(TextField txtPassword) {
			this.txtPassword = txtPassword;
		}
		
		//Getter & Setter für Stage & Controller
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
		// Getter & Setter für die CountryView
		public CreateView getCreateView() {
			return this.createView;
		}
		public void setCreateView(CreateView createView) {
			this.createView = createView;
		}
		
	protected Scene create_GUI() {
		ServiceLocator sl = ServiceLocator.getServiceLocator();
		Logger logger = sl.getLogger();

		MenuBar menuBar = new MenuBar();
		menuFile = new Menu();
		menuFileLanguage = new Menu();
		menuFile.getItems().add(menuFileLanguage);
		
		for (Locale locale : sl.getLocales()) {
			MenuItem language = new MenuItem(locale.getLanguage());
			menuFileLanguage.getItems().add(language);
			language.setOnAction(event -> {
				sl.getConfiguration().setLocalOption("Language", locale.getLanguage());
				sl.setTranslator(new Translator(locale.getLanguage()));
				updateTexts();
			});
		}

		menuHelp = new Menu();
		menuHelpShortcuts = new MenuItem("Shortcuts");
		menuHelp.getItems().add(menuHelpShortcuts);
		menuBar.getMenus().addAll(menuFile, menuHelp);

		GridPane root = new GridPane();
		root.add(menuBar, 0, 0);
		
		root.add(controlPaneLogin(), 0, 1);

		this.tableView = createTableView();
		root.add(tableView, 0, 2);
		this.tableView.prefWidthProperty().bind(root.widthProperty());

		root.add(controlPaneToDo(), 0, 3);

		// SZENEN
		createScene = new Scene(createView(), 450, 450);
				
		this.lblStatus = new Label("Everything okay");
		this.lblStatus.getStyleClass().add("statusLabel");
		root.add(this.lblStatus, 0, 4);

		
		updateTexts();

		Scene scene = new Scene(root);
		return scene;
	}

	
	
	protected void updateTexts() {
		Translator t = ServiceLocator.getServiceLocator().getTranslator();

		// The menu entries
		menuFile.setText(t.getString("program.menu.file"));
		menuFileLanguage.setText(t.getString("program.menu.language"));
		menuHelp.setText(t.getString("program.menu.help"));
		menuHelpShortcuts.setText(t.getString("program.menu.help.shortcuts"));
		
		// controls
		txtIP.setText(t.getString("txt.IP"));
		txtPort.setText(t.getString("txt.port"));
		txtUsername.setText(t.getString("txt.user"));
		txtPassword.setText(t.getString("txt.password"));

		btnLogin.setText(t.getString("button.login"));
		btnCreate.setText(t.getString("button.create"));
		btnDelete.setText(t.getString("button.delete"));
		btnLogout.setText(t.getString("button.logout"));

		stage.setTitle(t.getString("program.name"));

		 // Labels CreateView
		 getCreateView().getLblTitle().setText(t.getString("createView.label.title"));
		 getCreateView().getLblToDo().setText(t.getString("createView.label.toDo"));
		 getCreateView().getLblDueDate().setText(t.getString("createView.label.dueDate"));
		 getCreateView().getLblPriority().setText(t.getString("createView.label.priority"));
		 
		 // Buttons CreateView
		 getCreateView().getBtnSave().setText(t.getString("createView.button.save"));
		 getCreateView().getBtnCancel().setText(t.getString("createView.button.cancel"));
		
	}





}