import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
	private static UserList userList = new UserList();
	private User active;
	private SimpleBooleanProperty changeOc = new SimpleBooleanProperty(false);
	
	private void changeSignal() {
		changeOc.setValue(!changeOc.getValue());
	}
	
	private void save() throws IOException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("save.dat"));) {
			out.writeObject(userList);
		}
	}
	
	private void load() throws IOException, ClassNotFoundException, FileNotFoundException {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("save.dat"))){
			Object load = in.readObject();
			if (!(load instanceof UserList)) throw new IOException();
			else userList = (UserList) load;
		}
	}
	
	public void registerPane() {
		GridPane masterPane2 = new GridPane();
		GridPane childPane2 = new GridPane();
		Text regTitle = new Text("Register a new account");
		regTitle.setFont(new Font(20));
		Label newUsername = new Label("New username");
		TextField newUsernameIn = new TextField();
		Label newPassword = new Label("New password");
		TextField newPasswordIn = new TextField();
		Button back = new Button("Back");
		Button newRegister = new Button("Register");
		masterPane2.add(regTitle,0,0);
		GridPane.setHalignment(regTitle, HPos.CENTER);
		GridPane.setValignment(regTitle, VPos.CENTER);
		childPane2.add(newUsername, 0, 0);
		childPane2.add(newUsernameIn, 1, 0);
		childPane2.add(newPassword, 0, 1);
		childPane2.add(newPasswordIn, 1, 1);
		childPane2.add(back, 0, 2);
		childPane2.add(newRegister, 1, 2);
		childPane2.setVgap(25);
		childPane2.setHgap(20);
		GridPane.setHalignment(newRegister, HPos.RIGHT);
		masterPane2.add(childPane2, 0, 1);
		masterPane2.setVgap(50);
		
		FlowPane registerPane = new FlowPane();
		registerPane.setAlignment(Pos.CENTER);
		registerPane.getChildren().add(masterPane2);
		
		Scene newScene = new Scene(registerPane, 600, 500);
		
		Stage newStage = new Stage();
		newStage.setTitle("Financial Assistant - Register");
		newStage.setScene(newScene);
		newStage.show();
		
		newRegister.setOnAction(f -> {
			String usernameInput = newUsernameIn.getText();
			String passwordInput = newPasswordIn.getText();
			
			if (userList.validate(usernameInput)) {
				userList.register(usernameInput, passwordInput);
				popup("Registered!");
			} else {
				popup("Invalid username or username already taken!");
			}
			
			newUsernameIn.clear();
			newPasswordIn.clear();
		});
		
		back.setOnAction(e -> {newStage.hide(); start(new Stage());});
	}
	
	public void popup(String message) {
		Stage okStage = new Stage();
		GridPane okPane = new GridPane();
		Label toDisplay = new Label(message);
		Button button = new Button("OK");
		okPane.add(toDisplay,0,0);
		okPane.add(button, 0, 1);
		okPane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(toDisplay, HPos.CENTER);
		GridPane.setValignment(toDisplay, VPos.CENTER);
		GridPane.setHalignment(button, HPos.CENTER);
		GridPane.setValignment(button, VPos.CENTER);
		okPane.setVgap(20);
		button.setOnAction(e -> okStage.close());
		Scene okScene = new Scene(okPane, 250, 200);
		okStage.setTitle("Financial Assistant - Popup");
		okStage.setScene(okScene);
		okStage.show();
	}
	
	public void userPane(String username) {
		active = userList.get(username);
		
		GridPane masterPane = new GridPane();
		GridPane buttonsPane = new GridPane();
		
		Button logout = new Button("Logout");
		Button add = new Button("Add");
		Button change = new Button("Change");
		Button remove = new Button("Remove");
		
		buttonsPane.add(add, 0, 0);
		buttonsPane.add(change, 0, 1);
		buttonsPane.add(remove, 0, 2);
		buttonsPane.add(logout, 0, 3);
		
		buttonsPane.setVgap(20);
		buttonsPane.setAlignment(Pos.CENTER);
		
		ListView<String> accountList = new ListView<>(active.displayable());
		
		changeOc.addListener(new InvalidationListener() {
			public void invalidated(Observable o) {
				ListView<String> newAccountList = new ListView<>(active.displayable());
				masterPane.add(newAccountList, 0, 0);
				change.setOnAction(e -> {
					try {
						changePane(active.getAccounts().get(Account.parseName(newAccountList.getSelectionModel().getSelectedItem())));
					} catch (NullPointerException ex) {
						popup("An error occurred! Try again.");
					}
				});
				remove.setOnAction(e -> {
					try {
						removePane(active.getAccounts().get(Account.parseName(newAccountList.getSelectionModel().getSelectedItem())));
					} catch (NullPointerException ex) {
						popup("An error occurred! Try again.");
					}
				});
			}
		});
		
		masterPane.add(accountList, 0, 0);
		masterPane.add(buttonsPane, 1, 0);
		masterPane.setAlignment(Pos.CENTER);
		masterPane.setHgap(100);
		
		Scene userScene = new Scene(masterPane, 600, 500);
		
		Stage userStage = new Stage();
		
		add.setOnAction(e -> addPane());
		change.setOnAction(e -> {
			try {
				changePane(active.getAccounts().get(Account.parseName(accountList.getSelectionModel().getSelectedItem())));
			} catch (NullPointerException ex) {
				popup("An error occurred! Try again.");
			}
		});
		remove.setOnAction(e -> {
			try {
				removePane(active.getAccounts().get(Account.parseName(accountList.getSelectionModel().getSelectedItem())));
			} catch (NullPointerException ex) {
				popup("An error occurred! Try again.");
			}
		});
		logout.setOnAction(e -> {userStage.hide(); start(new Stage());});
		
		userStage.setTitle("Financial Assistant");
		userStage.setScene(userScene);
		userStage.show();
	}
	
	public void addPane() {
		GridPane pane = new GridPane();
		GridPane master = new GridPane();
		Button add = new Button("Add");
		Button cancel = new Button("Cancel");
		TextField accountNameIn = new TextField();
		TextField balanceIn = new TextField();
		TextField typeIn = new TextField();
		TextField interestIn = new TextField();
		pane.add(new Text("Account name"), 0, 0);
		pane.add(accountNameIn, 1, 0);
		pane.add(new Text("Balance"), 0, 1);
		pane.add(balanceIn, 1, 1);
		pane.add(new Text("Type"), 0, 2);
		pane.add(typeIn, 1, 2);
		pane.add(new Text("Interest rate"), 0, 3);
		pane.add(interestIn, 1, 3);
		pane.add(cancel, 0, 4);
		pane.add(add, 1, 4);
		
		pane.setHgap(20);
		pane.setVgap(20);
		pane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(add, HPos.RIGHT);
		
		Text addTitle = new Text("Add a new account");
		addTitle.setFont(new Font(20));
		master.add(addTitle, 0, 0);
		master.add(pane, 0, 1);
		GridPane.setHalignment(addTitle, HPos.CENTER);
		master.setAlignment(Pos.CENTER);
		master.setVgap(20);
		
		Stage stage = new Stage();
		Scene scene = new Scene(master, 500, 400);
		
		cancel.setOnAction(e -> stage.close());
		add.setOnAction(e -> {
			if (active.getAccounts().containsKey(accountNameIn.getText())) popup("Account already exists!");
			else {
				active.addAccount(accountNameIn.getText(), Double.parseDouble(balanceIn.getText()),
						typeIn.getText(), Double.parseDouble(interestIn.getText()));
				popup("Account added!");
				accountNameIn.clear();
				balanceIn.clear();
				typeIn.clear();
				interestIn.clear();
				changeSignal();
			}
		});
		
		stage.setTitle("Financial Assistant - Add account");
		stage.setScene(scene);
		stage.show();
	}
	
	public void changePane(Account toChange) {
		GridPane pane = new GridPane();
		GridPane master = new GridPane();
		Button change = new Button("Update");
		Button cancel = new Button("Cancel");
		TextField accountNameIn = new TextField(toChange.getName());
		TextField balanceIn = new TextField(toChange.getBalance() + "");
		TextField typeIn = new TextField(toChange.getType());
		TextField interestIn = new TextField(toChange.getInterestRate() + "");
		pane.add(new Text("New account name"), 0, 0);
		pane.add(accountNameIn, 1, 0);
		pane.add(new Text("New balance"), 0, 1);
		pane.add(balanceIn, 1, 1);
		pane.add(new Text("New type"), 0, 2);
		pane.add(typeIn, 1, 2);
		pane.add(new Text("New interest rate"), 0, 3);
		pane.add(interestIn, 1, 3);
		pane.add(cancel, 0, 4);
		pane.add(change, 1, 4);
		
		pane.setHgap(20);
		pane.setVgap(20);
		pane.setAlignment(Pos.CENTER);
		GridPane.setHalignment(change, HPos.RIGHT);
		
		Text changeTitle = new Text("Update an existing account");
		changeTitle.setFont(new Font(20));
		master.add(changeTitle, 0, 0);
		master.add(pane, 0, 1);
		GridPane.setHalignment(changeTitle, HPos.CENTER);
		master.setAlignment(Pos.CENTER);
		master.setVgap(20);
		
		Stage stage = new Stage();
		Scene scene = new Scene(master, 500, 400);
		
		cancel.setOnAction(e -> stage.close());
		change.setOnAction(e -> {
			boolean anythingChanged = false;
			
			if (!accountNameIn.getText().equals(toChange.getName()) &&
					!active.getAccountNames().contains(accountNameIn.getText())) {
				toChange.setName(accountNameIn.getText());
				anythingChanged = true;
			} else if (!accountNameIn.getText().equals(toChange.getName()) &&
					active.getAccountNames().contains(accountNameIn.getText()))
				popup("Duplicate account name!");
			
			if (Double.parseDouble(balanceIn.getText()) != toChange.getBalance()) {
				toChange.setBalance(Double.parseDouble(balanceIn.getText()));
				anythingChanged = true;
			}
			
			if (!typeIn.getText().equals(toChange.getType())) {
				toChange.setType(typeIn.getText());
				anythingChanged = true;
			}
			
			if (Double.parseDouble(interestIn.getText()) != toChange.getInterestRate()) {
				toChange.setInterestRate(Double.parseDouble(interestIn.getText()));
				anythingChanged = true;
			}
			
			if (anythingChanged) {
				popup("Updates made!");
				changeSignal();
			}
		});
		
		stage.setTitle("Financial Assistant - Update account");
		stage.setScene(scene);
		stage.show();
	}
	
	public void removePane(Account toRemove) {
		active.removeAccount(toRemove.getName());
		changeSignal();
	}
	
	public void start(Stage primaryStage) {
		GridPane masterPane = new GridPane();
		GridPane childPane = new GridPane();
		Text title = new Text("Financial Assistant");
		title.setFont(new Font(20));
		Label username = new Label("Username");
		TextField usernameIn = new TextField();
		Label password = new Label("Password");
		TextField passwordIn = new TextField();
		Button login = new Button("Log in");
		Button register = new Button("Register");
		Button save = new Button("Save");
		Button load = new Button("Load");
		masterPane.add(title,0,0);
		GridPane.setHalignment(title, HPos.CENTER);
		GridPane.setValignment(title, VPos.CENTER);
		childPane.add(username, 0, 0);
		childPane.add(usernameIn, 1, 0);
		childPane.add(password, 0, 1);
		childPane.add(passwordIn, 1, 1);
		childPane.add(login, 0, 2);
		childPane.add(register, 1, 2);
		childPane.add(save, 0, 3);
		childPane.add(load, 1, 3);
		childPane.setVgap(25);
		childPane.setHgap(20);
		GridPane.setHalignment(register, HPos.RIGHT);
		GridPane.setHalignment(load, HPos.RIGHT);
		masterPane.add(childPane, 0, 1);
		masterPane.setVgap(50);
		
		FlowPane pane = new FlowPane();
		pane.setAlignment(Pos.CENTER);
		pane.getChildren().add(masterPane);
		
		login.setOnAction(e -> {
				if (userList.authorize(usernameIn.getText(), passwordIn.getText())) {
					primaryStage.hide();
					userPane(usernameIn.getText());
				} else {
					popup("Invalid username and/or password!");
				}
			}
		);
		register.setOnAction(e -> {primaryStage.hide(); registerPane();});
		save.setOnAction(e -> {
			try {
				save();
				popup("Data saved!");
			} catch (IOException ex) {
				popup("Something's wrong! Data not saved.");
				System.out.println(ex);
			}
		});
		load.setOnAction(e -> {
			try {
				load();
				popup("Data loaded!");
			} catch (FileNotFoundException ex) {
				popup("Save file not found! Initializing anew.");
			} catch (IOException ex) {
				popup("Error reading file! Initializing anew.");
			} catch (ClassNotFoundException ex) {
				popup("Internal error! Initializing anew.");
			}
		});
		
		Scene scene = new Scene(pane, 600, 500);
		primaryStage.setTitle("Financial Assistant");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
