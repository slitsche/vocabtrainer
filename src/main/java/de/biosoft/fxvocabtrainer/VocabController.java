package de.biosoft.fxvocabtrainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.WindowEvent;

public class VocabController {
    @FXML
    TabPane tabPane;
    @FXML
    TabPane learnTabpane;
    @FXML
    ComboBox<String> cbFile;
    @FXML
    TextField txtQuestion;
    @FXML
    TextField txtAnswer;
    @FXML
    TableColumn<LearnItem, String> questionColumn;
    @FXML
    TableColumn<LearnItem, String> answerColumn;
    @FXML
    TableView<LearnItem> learnItemTable;

    private RepeatAndForget raf = new RepeatAndForget();
    private LearnItem currentItem;
    private StartPaneController startPaneController;
    private LearnPaneController learnPaneController;
    private ResultPaneController resultPaneController;

    // executed after injection of fxml elements
    public void initialize() {

        createTabPaneHandler();
        showStartPane();

        createItemTable();
        initFiles();

        // Make it look like a Wizard.
        // It was not obious how to hide the tabs in the tabpane.
        // But allow easy navigation in Scene Builder 
        // but it seems not to work with Java7
        // learnTabpane.setPadding(new Insets(-40, 0, 0, 0));
        initRaf();

    }

    private void createTabPaneHandler() {
        // TODO: check EventHandlerManager...
        tabPane.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST,
                new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        event.consume();
                        saveItems(cbFile.getSelectionModel().getSelectedItem());
                    }
                });
        tabPane.addEventFilter(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        if (event.getTarget() instanceof Node) {
                            String nodeId = ((Node) event.getTarget()).getId();
                            if ("btnStart".equals(nodeId)) {
                                startLearning(event);
                            } else if ("btnFalse".equals(nodeId)) {
                                doOnCancel(event);
                            } else if ("btnDone".equals(nodeId)) {
                                showStartPane();
                                initRaf();
                            }
                        }
                    }

                });
        tabPane.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        if (event.getTarget() instanceof Node) {
                            String nodeId = ((Node) event.getTarget()).getId();
                            System.out.println("tabPane> " + nodeId
                                    + event.getTarget().getClass().getName());
                            if ("btnOk".equals(nodeId)
                            // negation of isShowing: because we are in the
                            // event bubbling phase
                                    && !learnPaneController.isShowing()) {
                                doOnOk();

                            }
                        }
                    }

                });
        tabPane.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<Tab>() {

                    @Override
                    public void changed(ObservableValue<? extends Tab> arg0,
                            Tab arg1, Tab arg2) {
                        initRaf();
                    }

                });
    }

    private void showStartPane() {
        Node node;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/startpane.fxml"));
            node = (Node) loader.load();
            startPaneController = (StartPaneController) loader.getController();
            tabPane.getTabs().get(0).setContent(node);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void createItemTable() {
        learnItemTable.setItems(getLearnItemList());
        questionColumn.prefWidthProperty().bind(
                learnItemTable.widthProperty().divide(2));
        answerColumn.prefWidthProperty().bind(
                learnItemTable.widthProperty().divide(2));

        questionColumn
                .setCellValueFactory(new PropertyValueFactory<LearnItem, String>(
                        "question"));
        questionColumn.setCellFactory(TextFieldTableCell
                .<LearnItem> forTableColumn());
        questionColumn
                .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LearnItem, String>>() {

                    @Override
                    public void handle(CellEditEvent<LearnItem, String> ev) {
                        ((LearnItem) ev.getRowValue()).setQuestion(ev
                                .getNewValue());
                    }
                });
        answerColumn
                .setCellValueFactory(new PropertyValueFactory<LearnItem, String>(
                        "answer"));
        answerColumn.setCellFactory(TextFieldTableCell
                .<LearnItem> forTableColumn());
        answerColumn
                .setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LearnItem, String>>() {

                    @Override
                    public void handle(CellEditEvent<LearnItem, String> ev) {
                        ((LearnItem) ev.getRowValue()).setAnswer(ev
                                .getNewValue());
                    }
                });
    }

    private void initFiles() {
        List<String> list = LearnItemWriter.getBaseFileNames();
        ObservableList<String> l = FXCollections.observableList(list);
        cbFile.setItems(l);

        if (l.size() > 0) {
            cbFile.getSelectionModel().select(0);
            readItems(cbFile.getSelectionModel().getSelectedItem());
            initRaf();
        }

        cbFile.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<String>() {

                    @Override
                    public void changed(ObservableValue<? extends String> arg0,
                            String oldVal, String newVal) {
                        saveItems(oldVal);
                        readItems(newVal);

                    }
                });

    }

    protected void saveItems(String oldVal) {
        LearnItemWriter w = new LearnItemWriter(oldVal + ".txt");
        w.writeItems((List<LearnItem>) learnItemTable.getItems());
    }

    protected void readItems(String newVal) {
        ObservableList<LearnItem> items = learnItemTable.getItems();
        items.clear();
        LearnItemWriter w = new LearnItemWriter(newVal + ".txt"); // TODO
        List<LearnItem> i = w.getItems();
        items.addAll(i);
        initRaf();
    }

    private void initRaf() {
        raf.init((List<LearnItem>) learnItemTable.getItems());

        startPaneController.setGreeting(String.format(
                "%d neue Vokabeln zu üben", raf.getCount(RepeatAndForget.NEW)));
    }

    @FXML
    public void onNewAction(ActionEvent event) {
        String q = txtQuestion.getText();
        String a = txtAnswer.getText();
        if (q != null && a != null) {
            learnItemTable.getItems().add(new LearnItem(q, a));
            txtQuestion.setText("");
            txtAnswer.setText("");
        }
    }

    /**
     * First Action in Learn Wizard
     * 
     * @param Actionevent
     */
    @FXML
    public void startLearning(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/learnpane.fxml"));
            Node node = (Node) loader.load();
            tabPane.getTabs().get(0).setContent(node);
            learnPaneController = (LearnPaneController) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setNewQuestion();
    }

    private boolean setNewQuestion() {
        currentItem = null;
        currentItem = raf.next();
        if (currentItem != null) {
            learnPaneController.setLearnItem(currentItem);
            return true;
        }
        return false;

    }

    private void showResultsPage() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/resultpane.fxml"));
        try {
            Node node = (Node) loader.load();
            tabPane.getTabs().get(0).setContent(node);
            resultPaneController = loader.getController();
            resultPaneController.setTotalResult(String
                    .format("Vokabeln gesamt: %d",
                            raf.getLessonUniqueCount()));
            resultPaneController.setFailureResult(String
                    .format("Davon wiederholt: %d",
                            raf.getRepeatedCount()));

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void doOnOk() {
        currentItem.success();
        if (!setNewQuestion()) {
            showResultsPage();
        }
    }

    /**
     * This method works because it is fired in the event capturing phase.
     * 
     * @param e
     */
    private void doOnCancel(ActionEvent e) {
        if (learnPaneController.isShowing()) {
            currentItem.failure();
            raf.repeat(currentItem); // back into queue and repeat
            setNewQuestion();
        } else {
            showResultsPage();
        }

    }

    private ObservableList<LearnItem> getLearnItemList() {
        List<LearnItem> list = new ArrayList<LearnItem>();
        ObservableList<LearnItem> l = FXCollections.observableList(list);
        return l;
    }

}
