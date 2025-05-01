package viewmodel;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    private TextField regFirst, regLast, regEmail, regPassword, regVerify;
    @FXML
    private Label regNoti;
    @FXML
    private Button addAcct;

    String charPattern = "^.[a-z A-Z]{2,25}$";
    Pattern charactersOnly = Pattern.compile(charPattern);

    String emailPattern = "^[a-zA-Z0-9._%$!-]+@[a-zA-Z]+\\.(edu|com)$";
    Pattern pEmail = Pattern.compile(emailPattern);

    protected boolean charsValidCheck(String text) {
        return charactersOnly.matcher(text).matches();
    }

    protected boolean emailValidCheck(String e) {
        return pEmail.matcher(e).matches();
    }

    protected boolean passwordCheck(String p1, String p2) {
        if (p1.equals(p2) && p1 != null && p2 != null) {
            return true;
        } else {
            return false;
        }
    }

    protected void eligableTester() {
        if (charsValidCheck(regFirst.getText()) && charsValidCheck(regLast.getText()) &&
                emailValidCheck(regEmail.getText()) && passwordCheck(regPassword.getText(), regVerify.getText())) {

            addAcct.setDisable(false);
        } else {
            addAcct.setDisable(true);
        }
    }

    public static HashMap<String, String> userMap = new HashMap<>();

    @FXML
    protected void addAccountPressed() {

        regNoti.setText("Account created");
        regNoti.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> regNoti.setVisible(false));

        String firstName = regFirst.getText();
        String lastName = regLast.getText();
        String email = regEmail.getText();
        String password = regPassword.getText();

        userMap.put("firstName", firstName);
        userMap.put("lastName", lastName);
        userMap.put("email", email);
        userMap.put("password", password);
    }

    @FXML
    protected void regFirstTyped() {
        eligableTester();
    }

    @FXML
    protected void regLastTyped() {
        eligableTester();
    }

    @FXML
    protected void regEmailTyped() {
        eligableTester();
    }

    @FXML
    protected void regPasswordTyped() {
        eligableTester();
    }

    @FXML
    protected void regVerifyTyped() {
        eligableTester();
    }

    @FXML
    public void regGoBack(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource("/css/lightTheme.css").toExternalForm());
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
