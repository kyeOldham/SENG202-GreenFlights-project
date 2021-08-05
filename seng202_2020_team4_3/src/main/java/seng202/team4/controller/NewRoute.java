package seng202.team4.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;
import seng202.team4.model.DataType;
import seng202.team4.model.DatabaseManager;
import seng202.team4.model.Route;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Describes the functionality required for getting
 * a new route record.
 */
public class NewRoute extends NewRecord{
    @FXML
    private TextField airlineField;
    @FXML
    private ComboBox<String> depAirportCombo;
    /**
     * Mutable ObservableList containing a list of airport IATA and ICAO for the comboboxes.
     */
    private ObservableList<String> airports = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> dstAirportCombo;
    @FXML
    private JFXCheckBox codeshareField;
    @FXML
    private TextField stopsField;
    @FXML
    private TextField equipmentField;

    @FXML
    public void initialize() {
        initialiseComboBoxes();
    }

    private void initialiseComboBoxes() {
        String query = "SELECT DISTINCT ICAO, IATA FROM Airport";
        try (Connection connection = DatabaseManager.connect();
             Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ) {
            while (rs.next()) {
                String iata = rs.getString("IATA");
                String icao = rs.getString("ICAO");
                if (!(airports.contains(iata))) {
                    airports.add(iata);
                }
                if (!(airports.contains(icao))) {
                    airports.add(icao);
                }
            }
            FXCollections.sort(airports);
            depAirportCombo.setItems(airports);
            dstAirportCombo.setItems(airports);
            TextFields.bindAutoCompletion(depAirportCombo.getEditor(), depAirportCombo.getItems());
            TextFields.bindAutoCompletion(dstAirportCombo.getEditor(), dstAirportCombo.getItems());

        } catch(SQLException e) {
            ErrorController.createErrorMessage("Could not initialize airport combo boxes.", false);
        }
    }

    /**
     * Gets the content of the text fields in the scene.
     * @return a string array containing each of the text fields' content.
     */
    @Override
    String[] getRecordData() {
        String airline = airlineField.getText().trim();
        String srcAirport = depAirportCombo.getValue().trim();
        String dstAirport = dstAirportCombo.getValue().trim();
        String codeshare;
        if (codeshareField.isSelected()) {
            codeshare = "Y";
        } else {
            codeshare = "";
        }
        String stops = stopsField.getText().trim();
        String equipment = equipmentField.getText().trim();

        String[] recordData = {airline, srcAirport, dstAirport, codeshare, stops, equipment};
        return recordData;
    }

    /**
     * Sets the values of the route data in their corresponding text attribute.
     * @param data data that will be displayed.
     */
    @Override
    public void setRecordData(DataType data) {
        Route route = (Route) data;
        airlineField.setText(route.getAirlineCode());
        depAirportCombo.setValue(route.getSourceAirportCode());
        dstAirportCombo.setValue(route.getDestinationAirportCode());
        codeshareField.setSelected(route.isCodeshare());
        stopsField.setText(Integer.toString(route.getNumStops()));
        equipmentField.setText(route.getPlaneTypeCode());
    }

    @Override
    public void initialiseToolTips() {}
}
