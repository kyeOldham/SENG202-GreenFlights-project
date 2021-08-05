package seng202.team4.model;


import java.util.ArrayList;

/**
 * Describes the required implementation for Airline, Airport, Route and FlightPath classes.
 */
public abstract class DataType implements Validate {

    /**
     * Unique open flights identifier for this airline.
     */
    private int id;

    /**
     * The string required between attributes of the insert statement.
     */
    static final String BETWEEN = "', '";
    static final String UPDATE_BETWEEN = "', ";


    /**
     * Gets the database insert statement.
     * @param setID int the ID of the set the that will be inserted into.
     * @return String the insert statement.
     */
    public abstract String getInsertStatement(int setID);

    public abstract String getUpdateStatement(int setID);

    /**
     * Gets the name of data type.
     * @return String name of data type.
     */
    public abstract String getTypeName();

    /**
     * Gets the name of data type set.
     * @return String name of data type set.
     */
    public abstract String getSetName();

    /**
     * Gets a valid data type from the array of strings, or returns null with
     * reasons for being invalid in the error message.
     * @param record String[] array of strings constituting the record.
     * @param errorMessage ArrayList where the error messages will be stored.
     * @return DataType a valid new dataType, 'null' if invalid.
     */
    public abstract DataType getValid(String[] record, ArrayList<String> errorMessage);

    /**
     * Gets a valid data type from the array of strings, or returns null with
     * reasons for being invalid in the error message.
     * @param record String constituting the record.
     * @param errorMessage ArrayList where the error messages will be stored.
     * @return DataType a valid new dataType, 'null' if invalid.
     */
    public abstract DataType getValid(String record, ArrayList<String> errorMessage);

    /**
     * A method for checking if a given object is equal the current object of the class.
     * @param o Object
     * @return Returns true if equal to current object.
     */
    public abstract boolean equalsTest(Object o);

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }
}
