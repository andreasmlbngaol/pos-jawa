module com.jawa.utsposclient {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.jawa.utsposclient to javafx.fxml;
    exports com.jawa.utsposclient;
}