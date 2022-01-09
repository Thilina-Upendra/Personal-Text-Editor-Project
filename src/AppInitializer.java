import controller.TextEditorFormController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/TextEditorForm.fxml"));
        Parent parent = loader.load();

        /*============================================*/
        TextEditorFormController controller = loader.getController();
        /*============================================*/


        primaryStage.setScene(new Scene(parent));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
            if (!controller.txtTextArea.getText().isEmpty()) {
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to save this file??", ButtonType.YES, ButtonType.NO);
                    alert.setTitle("Save File");
                    Optional<ButtonType> type = alert.showAndWait();
                    if (type.get().equals(ButtonType.YES)) {
                        controller.mtmSaveOnAction(new ActionEvent());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
