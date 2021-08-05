package seng202.team4.controller;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Controls the behaviour of the invalid lines pop up when the user uploads a file with invalid lines
 */
public class InvalidLinesPopUpController {
    @FXML private JFXListView<String> listView;
    private Stage stage;
    private Stage parentStage;

    /**
     * Sets up the popup by setting assigning local variables
     * @param stage stage for the popup
     * @param parentStage stage of the window from which the popup was called
     */
    public void setUp(Stage stage, Stage parentStage) {
        this.stage = stage;
        this.parentStage = parentStage;
    }

    /**
     * Adds an ArrayList of strings to the listview to display the erroneous lines
     * @param errorLines error lines to display
     */
    public void addErrorLines(ArrayList<String> errorLines) {
        listView.setEditable(true);
        listView.getItems().addAll(errorLines);
    }

    /**
     * Closes the invalid lines pop up when the 'Okay' button is clicked
     */
    public void exit() {
        stage.close();
        parentStage.close();
    }
}
