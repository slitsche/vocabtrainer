package de.biosoft.fxvocabtrainer;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class VocabTrainerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pstage) throws Exception {
        init(pstage);
        pstage.show();

    }

    private void init(Stage pstage) throws IOException {
        pstage.setTitle("VocabTrainer");
        Parent root = FXMLLoader.load(getClass().getResource(
                "/vocabtrainer.fxml"));
        final Scene scene = new Scene(root);
        pstage.setScene(scene);
        pstage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                WindowEvent.fireEvent(scene.lookup("#tabPane"), event);
            }
        });

    }
}
