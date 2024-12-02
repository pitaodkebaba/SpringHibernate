package com.example.project.Frontend.Controllers;

import com.example.project.Frontend.Utils.HttpClientUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            String response = HttpClientUtil.login(username, password);
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", response);
        } catch (IOException | ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", e.getMessage());
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/Frontend/views/register.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Register");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}