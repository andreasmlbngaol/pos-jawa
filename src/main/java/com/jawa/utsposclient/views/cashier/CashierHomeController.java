package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.dto.Cashier;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;

public class CashierHomeController extends CashierController {

    @FXML private Button refundButton;
    @FXML private Button purchaseButton;
    @FXML private Button historyButton;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() throws IOException {
        Cashier user = (Cashier) JawaAuth.getInstance().getCurrent();

        if (user == null) {
            System.err.println("You are not logged in. Redirect to login page.");
            switchScene(AppScene.LOGIN);
        }

        addHoverEffect(refundButton);
        addHoverEffect(purchaseButton);
        addHoverEffect(historyButton);
        addHoverEffect(logoutButton);

    }




    @FXML
    private void onSwitchToPurchase() throws IOException {
        switchScene(AppScene.PURCHASE_TRANSACTION);
    }

    @FXML
    private void onSwitchToRefund() throws IOException {
        switchScene(AppScene.REFUND_TRANSACTION);
    }
}
