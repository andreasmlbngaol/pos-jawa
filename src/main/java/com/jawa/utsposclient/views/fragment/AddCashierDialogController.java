package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.repo.UserRepository;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AddCashierDialogController extends Controller {
    @FXML private TextField usernameTextField;
    @FXML private TextField nameTextField;

    @FXML
    public String onAddCashierAndGetOtp() {
        String username = usernameTextField.getText();
        String name = nameTextField.getText();

        if(!username.isEmpty() && !name.isEmpty()) {
            if(!UserRepository.isUsernameTaken(username)) {
                return UserRepository.addCashierAndGetOtp(username, name);
            }
        }

        return null;
    }
}
