package cz.algone;

import cz.algone.ui.main.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cz/algone/views/main.fxml"));
        Parent content = fxmlLoader.load();

        Scene scene = new Scene(content, 1280, 800);
        scene.getStylesheets().add(getClass().getResource("/cz/algone/styles/style.css").toExternalForm());

        // Okno bez systémového rámu — titulkovou lištu kreslí aplikace sama
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("PGRF1 2024/2025");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        MainViewController controller = fxmlLoader.getController();
        controller.initStage(stage);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
