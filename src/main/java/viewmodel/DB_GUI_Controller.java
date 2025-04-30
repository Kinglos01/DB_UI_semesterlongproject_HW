package viewmodel;

import dao.DbConnectivityClass;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Person;
import service.MyLogger;

import java.io.*;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
public class DB_GUI_Controller implements Initializable {

    // Adding the buttons so they can be modified to be enabled or disabled
    @FXML
    private Button addBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private ComboBox<String> majorBox;
    @FXML
    private Label notiLabel;
    @FXML
    private Pane importCSV;
    @FXML
    private Button exportCSVBtn;


    @FXML
    TextField first_name, last_name, department, email, imageURL;
    @FXML
    ImageView img_view;
    @FXML
    MenuBar menuBar;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_department, tv_major, tv_email;
    private final DbConnectivityClass cnUtil = new DbConnectivityClass();
    private final ObservableList<Person> data = cnUtil.getData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            tv_department.setCellValueFactory(new PropertyValueFactory<>("department"));
            tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
            tv_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tv.setItems(data);
            //adding the drop-down tab for the majors
            majorBox.getItems().addAll("CS","CPIS","English");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void addNewRecord(){

            Person p = new Person(first_name.getText(), last_name.getText(), department.getText(),
                    majorBox.getValue(), email.getText(), imageURL.getText());
            cnUtil.insertUser(p);
            cnUtil.retrieveId(p);
            p.setId(cnUtil.retrieveId(p));
            data.add(p);
            //Reset add button to be disabled after adding a person
            addBtn.setDisable(true);
            clearForm();
            //User added notification
            notiLabel.setVisible(true);
            notiLabel.setText("User added successfully.");
            newNotification();
    }


    @FXML
    protected void clearForm() {
        first_name.setText("");
        last_name.setText("");
        department.setText("");
       // major.setText("");
        email.setText("");
        imageURL.setText("");
        majorBox.setValue("Major");
        // on clear disabling the buttons again
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);
    }

    @FXML
    protected void logOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").getFile());
            Stage window = (Stage) menuBar.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeApplication() {
        System.exit(0);
    }

    @FXML
    protected void displayAbout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 500);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void editRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        Person p2 = new Person(index + 1, first_name.getText(), last_name.getText(), department.getText(),
                majorBox.getValue(), email.getText(),  imageURL.getText());
        cnUtil.editUser(p.getId(), p2);
        data.remove(p);
        data.add(index, p2);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void deleteRecord() {
        Person p = tv.getSelectionModel().getSelectedItem();
        int index = data.indexOf(p);
        cnUtil.deleteRecord(p);
        data.remove(index);
        tv.getSelectionModel().select(index);
    }

    @FXML
    protected void showImage() {
        File file = (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if (file != null) {
            img_view.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    protected void addRecord() {
        showSomeone();
    }

    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p = tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDepartment());
        //major.setText(p.getMajor());
        email.setText(p.getEmail());
        imageURL.setText(p.getImageURL());
        //added buttons being enabled on a selection
        editBtn.setDisable(false);
        deleteBtn.setDisable(false);
    }

    public void lightTheme(ActionEvent actionEvent) {
        try {
            Scene scene = menuBar.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.getScene().getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            System.out.println("light " + scene.getStylesheets());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void darkTheme(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) menuBar.getScene().getWindow();
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("/css/darkTheme.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showSomeone() {
        Dialog<Results> dialog = new Dialog<>();
        dialog.setTitle("New User");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField textField1 = new TextField("Name");
        TextField textField2 = new TextField("Last Name");
        TextField textField3 = new TextField("Email ");
        ObservableList<Major> options =
                FXCollections.observableArrayList(Major.values());
        ComboBox<Major> comboBox = new ComboBox<>(options);
        comboBox.getSelectionModel().selectFirst();
        dialogPane.setContent(new VBox(8, textField1, textField2,textField3, comboBox));
        Platform.runLater(textField1::requestFocus);
        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                return new Results(textField1.getText(),
                        textField2.getText(), comboBox.getValue());
            }
            return null;
        });
        Optional<Results> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((Results results) -> {
            MyLogger.makeLog(
                    results.fname + " " + results.lname + " " + results.major);
        });
    }

    private enum Major {Business, CSC, CPIS}

    private static class Results {

        String fname;
        String lname;
        Major major;

        public Results(String name, String date, Major venue) {
            this.fname = name;
            this.lname = date;
            this.major = venue;
        }
    }

    // Adding Regex to the different text boxes to be able to disable and enable the add button

        String charPattern = "^.[a-z A-Z]{2,25}$";
        Pattern charactersOnly =  Pattern.compile(charPattern);

        String emailPattern = "^[a-zA-Z0-9._%$!-]+@[a-zA-Z]+\\.(edu|com)$";
        Pattern pEmail =  Pattern.compile(emailPattern);


        protected boolean charsValidCheck(String text){
            return charactersOnly.matcher(text).matches();
        }

        protected boolean emailValidCheck(String e){
            return pEmail.matcher(e).matches();
        }

        protected String getFName(){return first_name.getText();}
        protected String getLName(){return last_name.getText();}
        protected String getEmail(){return email.getText();}
        protected String getDep(){return department.getText();}

        @FXML
        protected void fNameTyped(){
            eligibleChecker();
        }

        @FXML
        protected void lNameTyped(){
            eligibleChecker();
        }

        @FXML
        protected void emailTyped(){
            eligibleChecker();
        }

        @FXML
        protected void depTyped(){
            eligibleChecker();
        }

        protected void eligibleChecker(){
        if (charsValidCheck(getFName()) && charsValidCheck(getLName()) && emailValidCheck(getEmail()) && charsValidCheck(getDep()) && emailValidCheck(getEmail())) {
            addBtn.setDisable(false);
        }
        else{addBtn.setDisable(true);}
    }

    protected void newNotification(){
        notiLabel.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> notiLabel.setVisible(false));
    }

    // Making it possible to drag and drop csv file

    @FXML
    public void initialize() {
        importCSV.setOnDragOver(event -> {
            if (event.getGestureSource() != importCSV && event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
    }
        @FXML
        protected void csvDropped(DragEvent dragEvent) {
        Dragboard db = dragEvent.getDragboard();
        if (db.hasFiles()) {
            for (File file : db.getFiles()) {
                try {
                    importCSVMethod(file.getAbsolutePath());
                    notiLabel.setText("File imported successfully.");
                    newNotification();
                   // System.out.println("Dropped file: " + file.getAbsolutePath());

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {notiLabel.setText("File not found. Please try again.");
                newNotification();}
        }

        protected void importCSVMethod(String csvFilePath) throws FileNotFoundException {
            String csvLines;
            boolean firstline = true;
            try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
                while ((csvLines = br.readLine()) != null) {
                    if (firstline) {
                        firstline = false;
                        continue;
                    }
                    String[] temp = csvLines.split(",");
                    if (temp.length == 5) {
                        Person p = new Person();
                        p.setFirstName(temp[0]);
                        p.setLastName(temp[1]);
                        p.setDepartment(temp[2]);
                        p.setMajor(temp[3]);
                        p.setEmail(temp[4]);
                        cnUtil.insertUser(p);
                        cnUtil.retrieveId(p);
                        p.setId(cnUtil.retrieveId(p));
                        data.add(p);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        // allowing myself to export csv files

    @FXML
    private void exportCSV() throws FileNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.setInitialFileName("export.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File file = fileChooser.showSaveDialog(exportCSVBtn.getScene().getWindow());

        if (file != null) {
            exportTableToCSV(data, file.getAbsolutePath());
        }
    }

        @FXML
        public void exportTableToCSV(ObservableList<Person> list, String filePath) throws FileNotFoundException {
            try (PrintWriter writer = new PrintWriter(new File(filePath))) {
                StringBuilder sb = new StringBuilder();

                // Header
                sb.append("First,Last,Department,Major,Email\n");

                for (Person p : list) {
                    sb.append(p.getFirstName()).append(",");
                    sb.append(p.getLastName()).append(",");
                    sb.append(p.getDepartment()).append(",");
                    sb.append(p.getMajor()).append(",");
                    sb.append(p.getEmail()).append("\n");
                }

                writer.write(sb.toString());
        }

            catch (IOException e) {
            throw new RuntimeException(e);}
        }
}