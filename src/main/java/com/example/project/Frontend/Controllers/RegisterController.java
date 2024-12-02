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

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            String response = HttpClientUtil.register(username, email, password);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", response);
        } catch (IOException | ParseException e) {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", e.getMessage());
        }
    }

    @FXML
    void handleBackToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/Frontend/views/login.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Login");
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