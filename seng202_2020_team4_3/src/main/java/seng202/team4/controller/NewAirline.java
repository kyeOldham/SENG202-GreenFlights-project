package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import seng202.team4.model.Airline;
import seng202.team4.model.DataType;


/**
 * Describes the functionality required for getting
 * a new airline record.
 */
public class NewAirline extends NewRecord {
    @FXML private TextField nameField;
    @FXML private TextField codeField;
    @FXML private TextField iataField;
    @FXML private TextField icaoField;
    @FXML private TextField callSignField;
    @FXML private TextField countryField;
    @FXML private CheckBox recentlyActiveField;


    @FXML private Label aliasToolTip;
    @FXML private Label iataToolTip;
    @FXML private Label icaoToolTip;
    @FXML private Label callSignToolTip;

    /**
     * Initialises the tool tips for the new airline popup
     */
    @FXML
    public void initialize() {
        initialiseToolTips();
    }

    /**
     * Uses the CreateToolTip method from NewRecord to set the tool tips for the different entry fields
     */
    @Override
    public void initialiseToolTips() {
        createToolTip(aliasToolTip, "For example, All Nippon Airways is commonly known as \"ANA\"", false);
        createToolTip(iataToolTip, "International Air Transport Association number, usually a 3-letter code", false);
        createToolTip(icaoToolTip, "International Civil Aviation Organization, usually a 4-letter code", false);
        createToolTip(callSignToolTip, "Usually smaller, more recognisable version of Airline name", false);
    }


    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
    @Override
    String[] getRecordData() {
        String name = nameField.getText().trim();
        String code = codeField.getText().trim();
        String iata = iataField.getText().trim();
        String icao = icaoField.getText().trim();
        String callSign = callSignField.getText().trim();
        String country = countryField.getText().trim();
        String recentlyActive;
        if (recentlyActiveField.isSelected()) {
            recentlyActive = "Y";
        } else {
            recentlyActive = "N";
        }
        String[] recordData = {name, code, iata, icao, callSign, country, recentlyActive};
        return recordData;
    }

    @Override
    public void setRecordData(DataType data) {
        Airline airline = (Airline) data;
        nameField.setText(airline.getName());
        codeField.setText(airline.getAlias());
        iataField.setText(airline.getIata());
        icaoField.setText(airline.getIcao());
        callSignField.setText(airline.getCallSign());
        countryField.setText(airline.getCountry());
        recentlyActiveField.setSelected(airline.isRecentlyActive());
    }
}
