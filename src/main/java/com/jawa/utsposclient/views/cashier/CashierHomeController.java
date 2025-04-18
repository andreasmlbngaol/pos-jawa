package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.dto.Cashier;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.views.Controller;
import javafx.fxml.FXML;

import java.io.IOException;

public class CashierHomeController extends Controller {
    private Cashier user;

    @FXML
    private void initialize() throws IOException {
        this.user = (Cashier) JawaAuth.getInstance().getCurrent();

        if(user == null) {
            System.err.println("You are not logged in. Redirect to login page.");
            switchScene(AppScene.LOGIN);
        }
    }

//    @FXML
//    protected void onSignOutButtonClick() {
//        try {
//            var result = AuthRepository.logout();
//
//            if(result.isSuccess()) SessionManager.clearLocalSession(apiClient.getCookieManager());
//            else System.out.println(result.getMessage());
//
//            switchScene("/login-view.fxml", "Login");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
