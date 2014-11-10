package de.biosoft.fxvocabtrainer;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultPaneController {
    @FXML
    Label lblTotal;
    @FXML
    Label lblFailure;

    StringProperty totalProperty = new SimpleStringProperty();
    StringProperty failureProperty = new SimpleStringProperty();
    
    public void initialize(){
        lblTotal.textProperty().bind(totalProperty);
        lblFailure.textProperty().bind(failureProperty);
    }
    
    public void setTotalResult(String total){
        totalProperty.set(total);
    }
    public void setFailureResult(String msg){
        failureProperty.set(msg);
    }
}
