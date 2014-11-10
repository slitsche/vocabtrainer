package de.biosoft.fxvocabtrainer;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LearnPaneController {
    @FXML
    Label lblQuestion;
    @FXML
    Label lblAnswer;
    @FXML
    Button btnOk;
    @FXML
    Button btnFalse;
    private static final String SHOW = "Aufdecken";
    private static final String CANCEl = "Abbrechen";
    private static final String RIGHT = "Richtig";
    private static final String WRONG = "Falsch";

    ObjectProperty<LearnItem> currentItemProperty = new SimpleObjectProperty<LearnItem>();

    public void initialize() {
        toggleButtons();
        currentItemProperty.addListener(new ChangeListener<LearnItem>() {

            @Override
            public void changed(ObservableValue<? extends LearnItem> arg0,
                    LearnItem arg1, LearnItem newItem) {
                if (newItem != null) {
                    lblQuestion.setText(newItem.getQuestion());
                    lblAnswer.setText(newItem.getAnswer());
                }
            }
        });
    }

    public void setLearnItem(LearnItem item) {
        currentItemProperty.set(item);
    }

    @FXML
    public void doOnButton(ActionEvent e) {
        toggleButtons();
    }

    private void toggleButtons() {
        if (isShowing()) {
            btnOk.setText(SHOW);
            btnFalse.setText(CANCEl);
            lblAnswer.setVisible(false);
        } else {
            lblAnswer.setVisible(true);
            btnOk.setText(RIGHT);
            btnFalse.setText(WRONG);

        }
    }


    public boolean isShowing() {
        return lblAnswer.isVisible();
    }

}
