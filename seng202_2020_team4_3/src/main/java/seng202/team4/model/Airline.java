package seng202.team4.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Airline model class with attributes representing
 * the aspects of airlines. These attributes were chosen
 * from the open flights sample data. Objects of this class
 * are created to display in the table on the Airline tab.
 */
public class Airline extends DataType {


    /**
     * Name of the airline.
     */
    private String name;
    /**
     * Alias of the airline, a code.
     */
    private String alias;
    /**
     * 2-letter IATA code.
     */
    private String iata;
    /**
     * 3-letter ICAO code.
     */
    private String icao;
    /**
     * Airline callsign.
     */
    private String callSign;
    /**
     * Country or territory where airport is located.
     */
    private String country;
    /**
     * 'Y' if airline is or has been recently operational, 'N' otherwise.
     */
    private boolean recentlyActive;
    /**
     * Constant for length of an airline record
     * used in the validation of data input
     */
    private static final int AIRLINE_RECORD_LENGTH = 8;
    /**
     * Creates an empty object of airline.
     */
    public Airline() {}

    /**
     * Creates a full airline object.
     * @param name String Name of the airline
     * @param alias String Alias
     * @param iata String IATA
     * @param icao String ICAO
     * @param callSign String CallSign
     * @param country String Country
     * @param recentlyActive boolean Recently Active
     */
    public Airline(String name, String alias, String iata, String icao, String callSign, String country, boolean recentlyActive) {
        this.name = name;
        this.alias = alias;
        this.iata = iata;
        this.icao = icao;
        this.callSign = callSign;
        this.country = country;
        this.recentlyActive = recentlyActive;
    }

    /**
     * Gets the airline insert statement for the database.
     * @param setID int the ID of the set the that will be inserted into.
     * @return String the insert statement of a given ID statement.
     */
    @Override
    public String getInsertStatement(int setID){
        return "Insert into  " + getTypeName() + " ('Name', 'Alias', 'IATA', 'ICAO', 'CallSign', 'Country', 'RecentlyActive', 'SetId') "
                + "Values ('"
                + getName().replaceAll("'", "''") + BETWEEN
                + getAlias().replaceAll("'", "''") + BETWEEN
                + getIata().replaceAll("'", "''") + BETWEEN
                + getIcao().replaceAll("'", "''") + BETWEEN
                + getCallSign().replaceAll("'", "''") + BETWEEN
                + getCountry().replaceAll("'", "''") + BETWEEN
                + isRecentlyActive() + BETWEEN
                + setID
                + "');";
    }

    @Override
    public String getUpdateStatement(int setID) {
        return "Update " + getTypeName() + " set "
                + "Name='" + getName().replaceAll("'", "''") + UPDATE_BETWEEN
                + "Alias='" + getAlias().replaceAll("'", "''") + UPDATE_BETWEEN
                + "IATA='" + getIata().replaceAll("'", "''") + UPDATE_BETWEEN
                + "ICAO='" + getIcao().replaceAll("'", "''") + UPDATE_BETWEEN
                + "CallSign='" + getCallSign().replaceAll("'", "''") + UPDATE_BETWEEN
                + "Country='" + getCountry().replaceAll("'", "''") + UPDATE_BETWEEN
                + "RecentlyActive='" + isRecentlyActive() + UPDATE_BETWEEN
                + "SetId=" + setID
                + " where id=" + getId();
    }

    /**
     * Gets the datatype name.
     * @return String datatype name.
     */
    @Override
    public String getTypeName() {
        return "Airline";
    }

    /**
     * Gets the datatype set name.
     * @return String the datatype set name.
     */
    @Override
    public String getSetName() {
        return "AirlineSet";
    }

    /**
     * Gets a valid airline from the given strings. Fills
     * the error message list if any errors are encountered.
     * @param name String Name
     * @param code String Code
     * @param iata String IATA
     * @param icao String ICAO
     * @param callSign String CallSign
     * @param country String Country
     * @param recentlyActive String Recently Active
     * @param errorMessage ArrayList list of errors.
     * @return Airline the airline if valid, otherwise null.
     */
    public static Airline getValid(String name, String code, String iata, String icao, String callSign, String country, String recentlyActive, ArrayList<String> errorMessage) {
        boolean valid = true;

        if (!Validate.isAlphaMultiLanguage(name)) {
            errorMessage.add("Invalid name");
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(code) && !code.equals("\\N")) {
            errorMessage.add("Invalid alias");
            valid = false;
        }
        if (!Validate.isAirlineIATA(iata)) {
            errorMessage.add("Invalid IATA");
            valid = false;
        }
        if (!Validate.isAirlineICAO(icao)) {
            errorMessage.add("Invalid ICAO");
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(callSign)) {
            errorMessage.add("Invalid call sign");
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(country)) {
            errorMessage.add("Invalid country");
            valid = false;
        }
        recentlyActive = recentlyActive.toUpperCase();
        if (!recentlyActive.equals("Y") && !recentlyActive.equals("N")) {
            errorMessage.add("Invalid recently active");
            valid = false;
        }
        if (valid) {
            return new Airline(name, code, iata, icao, callSign, country, recentlyActive.equals("Y"));
        }
        else {
            return null;
        }
    }

    /**
     * Converts record array into individual strings and calls get valid.
     * @param record String[] array of strings constituting the record.
     * @param errorMessage ArrayList arrayList where the error messages will be stored.
     * @return Airline the airline if valid, otherwise null.
     */
    @Override
    public Airline getValid(String[] record, ArrayList<String> errorMessage) {
        String newName = record[0];
        String newCode = record[1];
        String newIata = record[2];
        String newIcao = record[3];
        String newCallSign = record[4];
        String newCountry = record[5];
        String newRecentlyActive = record[6];
        return getValid(newName, newCode, newIata, newIcao, newCallSign, newCountry, newRecentlyActive, errorMessage);
    }

    /**
     * Converts a string record into individual strings and calls get valid.
     * @param record String constituting the record.
     * @param errorMessage ArrayList where the error messages will be stored.
     * @return DataType the airline if valid, otherwise null.
     */
    @Override
    public DataType getValid(String record, ArrayList<String> errorMessage) {
        String[] recordList = record.replaceAll("\"", "").split(",");
        if (recordList.length != AIRLINE_RECORD_LENGTH) {
            errorMessage.add("Invalid number of attributes");
            return null;
        }
        recordList = Arrays.copyOfRange(recordList, 1, AIRLINE_RECORD_LENGTH);
        return getValid(recordList, errorMessage);
    }

    /**
     * A method for checking if a given object is equal the current object of the class.
     * @param o Object
     * @return Returns true if equal to current object.
     */
    @Override
    public boolean equalsTest(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airline)) return false;
        Airline airline = (Airline) o;
        return getId() == airline.getId() &&
                isRecentlyActive() == airline.isRecentlyActive() &&
                getName().equals(airline.getName()) &&
                getAlias().equals(airline.getAlias()) &&
                getIata().equals(airline.getIata()) &&
                getIcao().equals(airline.getIcao()) &&
                getCallSign().equals(airline.getCallSign()) &&
                getCountry().equals(airline.getCountry());
    }

    // getters
    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getIata() {
        return iata;
    }

    public String getIcao() {
        return icao;
    }

    public String getCallSign() {
        return callSign;
    }

    public String getCountry() {
        return country;
    }

    public boolean isRecentlyActive() {
        return recentlyActive;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setRecentlyActive(boolean recentlyActive) {
        this.recentlyActive = recentlyActive;
    }

}
