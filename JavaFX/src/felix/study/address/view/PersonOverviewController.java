package felix.study.address.view;

import org.controlsfx.dialog.Dialogs;

import felix.study.address.Main;
import felix.study.address.model.Person;
import felix.study.address.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PersonOverviewController {
	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> firstNameColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;

	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label streetLabel;
	@FXML
	private Label postalCodeLabel;
	@FXML
	private Label cityLabel;
	@FXML
	private Label birthdayLabel;

	private Main mainApp;

	public PersonOverviewController() {
	}

	@FXML
	private void initialize() {

		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue()
				.firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue()
				.lastNameProperty());
		showPerwonDetails(null);
		personTable
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showPerwonDetails(newValue));
	}

	public void setMainApp(Main mainApp) {
		this.mainApp = mainApp;

		personTable.setItems(mainApp.getPersonData());
	}

	public void showPerwonDetails(Person person) {
		if (person != null) {
			firstNameLabel.setText(person.getFirstName());
			lastNameLabel.setText(person.getLastName());
			streetLabel.setText(person.getStreet());
			cityLabel.setText(person.getCity());
			postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
			birthdayLabel.setText(DateUtil.format(person.getBirthday()));
		} else {
			firstNameLabel.setText("");
			lastNameLabel.setText("");
			streetLabel.setText("");
			cityLabel.setText("");
			postalCodeLabel.setText("");
			birthdayLabel.setText("");
		}
	}

	@FXML
	private void handleDeletePerson() {
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex > 0) {
			personTable.getItems().remove(selectedIndex);
		} else {
			Dialogs.create().title("No Selection!")
					.masthead("No person selected")
					.message("Please select a person in the table.")
					.showWarning();
		}
	}

	@FXML
	private void handleNewPerson() {
		Person person = new Person();
		boolean okClicked = mainApp.showPersonEditDialog(person);
		if (okClicked) {
			mainApp.getPersonData().add(person);
		}
	}

	@FXML
	private void handleEditPerson() {
		Person selected = personTable.getSelectionModel().getSelectedItem();
		boolean okClicked = mainApp.showPersonEditDialog(selected);
		if (selected != null) {
			if (okClicked) {
				showPerwonDetails(selected);
			}
		} else {
			Dialogs.create().title("No selection")
					.masthead("No person selected")
					.message("Please select person to edit").showError();
		}
	}

}
