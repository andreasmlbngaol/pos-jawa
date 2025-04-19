package com.jawa.utsposclient.views.fragment;

import com.jawa.utsposclient.dto.Admin;
import com.jawa.utsposclient.repo.UserRepository;
import com.jawa.utsposclient.utils.JawaAuth;
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
            if(!UserRepository.checkUsername(username)) {
                return ((Admin) JawaAuth.getInstance().getCurrent()).addCashierAndGetOtp(username, name);
            }
        }

        return null;
    }
}
