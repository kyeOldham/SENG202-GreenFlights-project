package seng202.team4.model;

/**
 * Interface for validating strings for data input into the application and database.
 */
public interface Validate {
    static final String NULL = "\\N";

    /**
     * Method for checking if a given string is a float.
     * @param string String
     * @return Returns true if the given string is a float.
     */
    static boolean isFloat(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Method for checking if a given string is an integer.
     * @param string String
     * @return Returns true if the given string is an integer.
     */
    static boolean isInteger(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("-?\\d+");
    }

    /**
     * Method for checking if a given string is an alpha.
     * @param string String
     * @return Returns true if the given string is an alpha.
     */
    static boolean isAlpha(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[A-Za-z][A-Za-z\\s]*");
    }

    /**
     * Method for checking if a given string is an alpha with multiple language characters.
     * @param string String
     * @return Returns true if the given string is an alpha with multiple language characters.
     */
    static boolean isAlphaMultiLanguage(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[-\\a-zA-Z0-9 \'.\u0080-\u9fff]*+");
    }

    /**
     * Method for checking if a given string is alphanumeric.
     * @param string String
     * @return Returns true if the given string is alphanumeric.
     */
    static boolean isAlphaNumeric(String string) {
        if (string == null) {
            return false;
        }
        return string.matches("[A-Za-z0-9\\s]*");
    }

    /**
     * Method for checking if a given string is an airport IATA.
     * @param string String
     * @return Returns true if the given string is an airport IATA.
     */
    static boolean isAirportIATA(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[a-zA-Z0-9а-яА-Я%]{3}");
    }

    /**
     * Method for checking if a given string is an airport ICAO.
     * @param string String
     * @return Returns true if the given string is an airport ICAO.
     */
    static boolean isAirportICAO(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[a-zA-Z0-9-_%]{3,4}");
    }

    /**
     * Method for checking if a given string is an airline IATA.
     * @param string String
     * @return Returns true if the given string is an airline IATA.
     */
    static boolean isAirlineIATA(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[-\\a-zA-Z0-9а-яА-Я +^;:&.?!]*");
    }

    /**
     * Method for checking if a given string is an airline ICAO.
     * @param string String
     * @return Returns true if the given string is an airline ICAO.
     */
    static boolean isAirlineICAO(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.equals(NULL) || string.matches("[-\\a-zA-Z0-9а-яА-Я '*/+=:&.?!]*");
    }

    /**
     * Method for checking if a given string is a valid timezone database.
     * @param string String
     * @return Returns true if the given string is a valid timezone database.
     */
    static boolean isValidTZDB(String string) {
        if (string == null) {
            return false;
        }
        return string.equals("") || string.matches("[a-zA-Z]+/[a-zA-Z_-]+") || string.equals(NULL);
    }

    /**
     * Method for checking if a given string is a valid timezone.
     * @param string String
     * @return Returns true if the given string is a valid timezone.
     */
    static boolean isValidTimeZone(String string) {
        try {
            double timezone = Double.parseDouble(string);
            if (timezone >= -12 && timezone <= 14) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method for checking if a given string is an ASCII or null.
     * @param string String
     * @return Returns true if the given string is an ASCII or null.
     */
    static boolean isAsciiOrNull(String string) {
        return string.equals(NULL) || string.matches("^\\p{ASCII}*$");
    }
}
