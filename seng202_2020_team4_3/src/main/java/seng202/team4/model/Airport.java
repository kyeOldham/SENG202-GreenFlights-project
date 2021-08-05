package seng202.team4.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Airport model class with attributes representing
 * the aspects of airports. These attributes were chosen
 * from the open flights sample data. Objects of this class
 * are created to display in the table on the Airport tab.
 */
public class Airport extends DataType {

    /**
     * name of airport.
     */
    private String name;
    /**
     * Main city server by airport.
     */
    private String city;
    /**
     * Country or territory where airport is located
     */
    private String country;
    /**
     * 3-letter IATA code
     */
    private String iata;
    /**
     * 4-letter ICAO code
     */
    private String icao;
    /**
     * Decimal degrees for latitude of airprot to six significant figures, Negative is south, positive is north
     */
    private double latitude;
    /**
     * Decimal degrees for longitude of airprot to six significant figures, Negative is west, positive is east
     */
    private double longitude;
    /**
     * String representing latitude and longitude together
     */
    private String coordinates;
    /**
     * altitude of airport in feet
     */
    private int altitude;
    /**
     * Time zone representing hours offset from UTC
     */
    private float timezone;
    /**
     * Daylight savings time
     */
    private char dst;
    /**
     * Time zone in 'tz' (Olson) format
     */
    private String tzDatabase;
    /**
     *  Type of the airport
     */
    private String type;
    /**
     * Source of this data, Open flights
     */
    private String source;
    /**
     * Boolean property that is true if the airport has been selected with the checkbox and false otherwise
     */
    private BooleanProperty select = new SimpleBooleanProperty(false);
    /**
     * Constant for length of an airport record
     * used in the validation of data input
     */
    private static final int AIRPORT_RECORD_LENGTH = 13;
    /**
     * Creates an empty object of airport.
     */
    public Airport() {}

    /**
     * Creates a full airport object.
     * @param name String Name
     * @param city String City
     * @param country String Country
     * @param iata String IATA
     * @param icao String ICAO
     * @param latitude double Latitude
     * @param longitude double Longitude
     * @param altitude double Altitude
     * @param timeZone float TimeZone
     * @param dst char DST
     * @param tzDatabase String TimeZone Database
     */
    public Airport (String name, String city, String country, String iata, String icao, double latitude, double longitude, int altitude, float timeZone, char dst, String tzDatabase) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.iata = iata;
        this.icao = icao;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timezone = timeZone;
        this.dst = dst;
        this.tzDatabase = tzDatabase;
        setCoordinates(longitude, latitude);
    }

    /**
     * Gets the airport insert statement for the database.
     * @param setID the ID of the set the that will be inserted into.
     * @return The insert statement of a given ID.
     */
    @Override
    public String getInsertStatement(int setID) {
        return "Insert into  " + getTypeName() + " ('Name', 'City', 'Country', 'IATA', 'ICAO', 'Latitude', 'Longitude', 'Altitude', 'TimeZone', 'DST', 'TzDatabaseTime', 'SetId') "
                + "Values ('"
                + getName().replaceAll("'", "''''") + BETWEEN
                + getCity().replaceAll("'", "''") + BETWEEN
                + getCountry().replaceAll("'", "''") + BETWEEN
                + getIata().replaceAll("'", "''") + BETWEEN
                + getIcao().replaceAll("'", "''") + BETWEEN
                + getLatitude() + BETWEEN
                + getLongitude() + BETWEEN
                + getAltitude() + BETWEEN
                + getTimezone() + BETWEEN
                + getDst() + BETWEEN
                + getTzDatabase().replaceAll("'", "''") + BETWEEN
                + setID
                + "');";
    }

    @Override
    public String getUpdateStatement(int setID) {
        return "Update " + getTypeName() + " set "
                + "Name='" + getName().replaceAll("'", "''''") + UPDATE_BETWEEN
                + "City='" + getCity().replaceAll("'", "''") + UPDATE_BETWEEN
                + "Country='" + getCountry().replaceAll("'", "''") + UPDATE_BETWEEN
                + "IATA='" + getIata().replaceAll("'", "''") + UPDATE_BETWEEN
                + "ICAO='" + getIcao().replaceAll("'", "''") + UPDATE_BETWEEN
                + "Latitude='" + getLatitude() + UPDATE_BETWEEN
                + "Longitude='" + getLongitude() + UPDATE_BETWEEN
                + "Altitude='" + getAltitude() + UPDATE_BETWEEN
                + "TimeZone='" + getTimezone() + UPDATE_BETWEEN
                + "DST='" + getDst() + UPDATE_BETWEEN
                + "TzDatabaseTime='" + getTzDatabase().replaceAll("'", "''") + UPDATE_BETWEEN
                + "SetId=" + setID
                + " where id=" + getId();
    }

    /**
     * Gets the datatype name.
     * @return String datatype name.
     */
    @Override
    public String getTypeName() {
        return "Airport";
    }

    /**
     * Gets the datatype set name.
     * @return String datatype set name.
     */
    @Override
    public String getSetName() {
        return "AirportSet";
    }

    /**
     * Gets a valid airline from the given strings. Fills
     * the error message list if any errors are encountered.
     * @param name String Name
     * @param city String City
     * @param country String Country
     * @param iata String IATA
     * @param icao String ICAO
     * @param latitude String Latitude
     * @param longitude String Longitude
     * @param altitude String Altitude
     * @param timeZone String TimeZone
     * @param dst String DST
     * @param tzDatabase String TimeZone Database
     * @param errorMessage ArrayList list of errors.
     * @return the airport if valid, otherwise null.
     */
    public static Airport getValid(String name, String city, String country, String iata, String icao, String latitude, String longitude, String altitude, String timeZone, String dst, String tzDatabase, ArrayList<String> errorMessage) {
        boolean valid = true;
        if (!Validate.isAlphaMultiLanguage(name)) {
            errorMessage.add("Invalid name");
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(city)) {
            errorMessage.add("Invalid city");
            valid = false;
        }
        if (!Validate.isAlphaMultiLanguage(country)) {
            errorMessage.add("Invalid country");
            valid = false;
        }
        if (!Validate.isAirportIATA(iata)) {
            errorMessage.add("Invalid IATA");
            valid = false;
        }
        if (!Validate.isAirportICAO(icao)) {
            errorMessage.add("Invalid ICAO");
            valid = false;
        }
        if (!Validate.isFloat(latitude)) {
            errorMessage.add("Invalid latitude");
            valid = false;
        }
        if (!Validate.isFloat(longitude)) {
            errorMessage.add("Invalid longitude");
            valid = false;
        }
        if (!Validate.isInteger(altitude)) {
            errorMessage.add("Invalid altitude");
            valid = false;
        }
        if (!Validate.isValidTimeZone(timeZone)) {
            errorMessage.add("Invalid time zone");
            valid = false;
        }

        char dstChar = 'N';
        if (dst == null) {
            errorMessage.add("Invalid daylight savings time");
            valid = false;
        }
        else if (dst.length() == 1) {
            dstChar = dst.charAt(0);
            if (!(dstChar == 'E' || dstChar == 'A' || dstChar == 'S' || dstChar == 'O' || dstChar == 'Z' || dstChar == 'N' || dstChar == 'U')) {
                errorMessage.add("Invalid daylight savings time");
                valid = false;
            }
        }
        else {
            errorMessage.add("Invalid daylight savings time");
            valid = false;
        }

        if (!Validate.isValidTZDB(tzDatabase)) {
            errorMessage.add("Invalid tz database");
            valid = false;
        }

        if (valid) {
            return new Airport(name, city, country, iata, icao, Double.parseDouble(latitude), Double.parseDouble(longitude), Integer.parseInt(altitude), Float.parseFloat(timeZone), dstChar, tzDatabase);
        }
        else {
            return null;
        }

    }

    /**
     * Converts record array into individual strings and calls get valid.
     * @param record String[] array of strings constituting the record.
     * @param errorMessage ArrayList arrayList where the error messages will be stored.
     * @return DataType the airport if valid, otherwise null.
     */
    @Override
    public DataType getValid(String[] record, ArrayList<String> errorMessage) {
        String newName = record[0];
        String newCity = record[1];
        String newCountry = record[2];
        String newIata = record[3];
        String newIcao = record[4];
        String newLatitude = record[5];
        String newLongitude = record[6];
        String newAltitude = record[7];
        String newTimeZone = record[8];
        String newDst = record[9];
        String newTzDatabase = record[10];

        return getValid(newName, newCity, newCountry, newIata, newIcao, newLatitude, newLongitude, newAltitude, newTimeZone, newDst, newTzDatabase, errorMessage);
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
        String[] newRecordList = new String[AIRPORT_RECORD_LENGTH - 1];
        boolean containsComma = false;
        if (recordList.length == AIRPORT_RECORD_LENGTH) {
            containsComma = true;
            newRecordList[0] = recordList[0];
            newRecordList[1] = recordList[1];
            newRecordList[2] = recordList[2] + " " + recordList[3];
            newRecordList[3] = recordList[4];
            newRecordList[4] = recordList[5];
            newRecordList[5] = recordList[6];
            newRecordList[6] = recordList[7];
            newRecordList[7] = recordList[8];
            newRecordList[8] = recordList[9];
            newRecordList[9] = recordList[10];
            newRecordList[10] = recordList[11];
            newRecordList[11] = recordList[12];
        }
        if (recordList.length != (AIRPORT_RECORD_LENGTH - 1) && recordList.length != AIRPORT_RECORD_LENGTH) {
            errorMessage.add("Invalid number of attributes");
            return null;
        }
        if (containsComma) {
            newRecordList = Arrays.copyOfRange(newRecordList, 1, AIRPORT_RECORD_LENGTH - 1);
            return getValid(newRecordList, errorMessage);
        }
        else {
            recordList = Arrays.copyOfRange(recordList, 1, AIRPORT_RECORD_LENGTH - 1);
            return getValid(recordList, errorMessage);
        }
    }

    /**
     * A method for checking if a given object is equal the current object of the class.
     * @param o Object
     * @return Returns true if equal to current object.
     */
    @Override
    public boolean equalsTest(Object o) {
        if (this == o) return true;
        if (!(o instanceof Airport)) return false;
        Airport airport = (Airport) o;
        return getId() == airport.getId() &&
                Double.compare(airport.getLatitude(), getLatitude()) == 0 &&
                Double.compare(airport.getLongitude(), getLongitude()) == 0 &&
                Double.compare(airport.getAltitude(), getAltitude()) == 0 &&
                Float.compare(airport.getTimezone(), getTimezone()) == 0 &&
                getDst() == airport.getDst() &&
                getName().equals(airport.getName()) &&
                getCity().equals(airport.getCity()) &&
                getCountry().equals(airport.getCountry()) &&
                getIata().equals(airport.getIata()) &&
                getIcao().equals(airport.getIcao()) &&
                getTzDatabase().equals(airport.getTzDatabase());
    }

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = Precision.round(latitude, 2);
        setCoordinates(this.longitude, this.latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = Precision.round(longitude, 2);
        setCoordinates(this.longitude, this.latitude);
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public float getTimezone() {
        return timezone;
    }

    public void setTimezone(float timezone) {
        this.timezone = timezone;
    }

    public char getDst() {
        return dst;
    }

    public void setDst(char dst) {
        this.dst = dst;
    }

    public String getTzDatabase() {
        return tzDatabase;
    }

    public void setTzDatabase(String tzDatabase) {
        this.tzDatabase = tzDatabase;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setCoordinates(double longitude, double latitude) {
        this.coordinates = String.format("%.2f, %.2f", longitude, latitude);
    }

    public String getCoordinates() {
        return coordinates;
    }

    public boolean isSelect() {
        return select.get();
    }

    public BooleanProperty selectProperty() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select.set(select);
    }
}
