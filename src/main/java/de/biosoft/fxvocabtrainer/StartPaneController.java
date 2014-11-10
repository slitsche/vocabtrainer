package de.biosoft.fxvocabtrainer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartPaneController {
    @FXML
    Label lblVocab;

    StringProperty newVocabProp = new SimpleStringProperty();

    public void initialize() {
        lblVocab.textProperty().bind(newVocabProp);
    }
    
    public void setGreeting(String greet){
        newVocabProp.set(greet);
    }

    // lblVocab.setText(String.format("%d neue Vokabeln zu �ben",
    // raf.getCount(RepeatAndForget.NEW)));

}
