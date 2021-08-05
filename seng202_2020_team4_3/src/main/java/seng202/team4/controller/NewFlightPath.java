package seng202.team4.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import seng202.team4.model.DataType;
import seng202.team4.model.FlightPath;
import seng202.team4.model.Path;

/**
 * Describes the functionality required for getting
 * a new flight path record.
 */
public class NewFlightPath extends NewRecord {
    @FXML
    private TextField typeField;
    @FXML
    private TextField flightPathIdField;
    @FXML
    private TextField altitudeField;
    @FXML
    private TextField latitudeField;
    @FXML
    private TextField longitudeField;
    @FXML
    private Label typeToolTip;


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
        createToolTip(typeToolTip, Path.FLIGHTPATH_TYPE, true);
    }
    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
    @Override
    String[] getRecordData() {
        String type = typeField.getText().trim();
        String id = flightPathIdField.getText().trim();
        String altitude = altitudeField.getText().trim();
        String latitude = latitudeField.getText().trim();
        String longitude = longitudeField.getText().trim();
        String[] recordData = {type, id, altitude, latitude, longitude};
        return recordData;
    }

    @Override
    public void setRecordData(DataType data) {
        FlightPath path = (FlightPath) data;
        typeField.setText(path.getType());
        flightPathIdField.setText(path.getFlightPathId());
        latitudeField.setText(Double.toString(path.getLatitude()));
        longitudeField.setText(Double.toString(path.getLongitude()));
        altitudeField.setText(Double.toString(path.getAltitude()));
    }
}
