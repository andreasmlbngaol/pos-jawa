package com.jawa.utsposclient.views.cashier;

import com.jawa.utsposclient.dto.Cashier;
import com.jawa.utsposclient.enums.AppScene;
import com.jawa.utsposclient.utils.JawaAuth;
import com.jawa.utsposclient.utils.JawaButton;
import com.jawa.utsposclient.utils.StringRes;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import java.io.IOException;

public class CashierHomeController extends CashierController {

    @FXML private VBox refundVBox;
    @FXML private VBox purchaseVBox;
    @FXML private VBox historyVBox;
    @FXML private Button logoutButton;

    @FXML
    private void initialize() throws IOException {
        Cashier user = (Cashier) JawaAuth.getInstance().getCurrent();

        if (user == null) {
            System.err.println("You are not logged in. Redirect to login page.");
            switchScene(AppScene.LOGIN);
        }

        logoutButton.setGraphic(JawaButton.createExtendedFab(
            MaterialDesign.MDI_LOGOUT,
            StringRes.get("logout_button"),
            Color.web("#e8b323"),
            Color.WHITE,
            Color.WHITE
        ));

        addHoverEffect(refundVBox);
        addHoverEffect(purchaseVBox);
        addHoverEffect(historyVBox);
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
