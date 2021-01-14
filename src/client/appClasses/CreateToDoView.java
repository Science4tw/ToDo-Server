package client.appClasses;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.Priority;

// 0
public class CreateToDoView extends GridPane {

	private App_Model model;
	private App_Controller controller;
	private Stage stage;

	// 1 Controls used for data processing (Data Entry)
	private Label lblTitle;
	private Label lblToDo;
	private Label lblDescription;
	private Label lblPriority;

	// 1
	// Textfeld für To Do, Description, Due Date
	private TextField txtToDo = new TextField();
	private TextField txtDescription = new TextField();

	// ComboBox für Priority
	protected ComboBox<Priority> cmbPriority = new ComboBox<Priority>();

	// 1 Buttons 
	protected Button btnSave = new Button("Save");
	protected Button btnCancel = new Button("Cancel");

	// Konstruktor
	public CreateToDoView(Stage stage, App_Model model, App_Controller controller) {
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
		lblTitle = new Label("To Do erstellen");
		lblTitle.getStyleClass().add("titlelabel");


		lblToDo = new Label("To Do");
		lblDescription = new Label("Description");
		lblPriority = new Label("Priorität");
		// Fill combos (hol mir die Items,alle hinzufügen von den values der Enums)
		cmbPriority.getItems().addAll(Priority.values());

		// Organize the layout, add in the controls (col, row)
		pane.add(lblTitle, 0, 0);

		pane.add(lblToDo, 0, 1);
		pane.add(txtToDo, 1, 1);

		pane.add(lblDescription, 0, 2);
		pane.add(txtDescription, 1, 2);

	
		pane.add(lblPriority, 0, 3);
		pane.add(cmbPriority, 1, 3);

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
		this.txtToDo.setText("");
		this.txtDescription.setText("");
		this.cmbPriority.getSelectionModel().clearSelection();
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

	// Getter Fields
	public TextField getTxtToDo() {
		return txtToDo;
	}

	
	public TextField getTxtDescription() {
		return txtDescription;
	}
	public ComboBox<Priority> getCmbPriority() {
		return cmbPriority;
	}
	// Setter Fields
	public void setTxtToDo(TextField txtToDo) {
		this.txtToDo = txtToDo;
	}


	public void setTxtDescription(TextField txtDescription) {
		this.txtDescription = txtDescription;
	}
	
	public void setCmbPriority(ComboBox<Priority> cmbPriority) {
		this.cmbPriority = cmbPriority;
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

	public Label getLblToDo() {
		return lblToDo;
	}

	public Label getLblPriority() {
		return lblPriority;
	}


}
