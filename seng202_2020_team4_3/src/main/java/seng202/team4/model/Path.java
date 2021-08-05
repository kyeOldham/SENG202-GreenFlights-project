package seng202.team4.model;

/**
 * Holds static string values for resources and directories used throughout the application, e.g. FXML files.
 */
public abstract class Path {
    public static final String DIRECTORY = System.getProperty("user.dir") + "/GreenFlights_Resources";
    public static final String DATABASE = DIRECTORY + "/DATABASE.db";
    public static final String DATABASE_CONNECTION = "jdbc:sqlite:" + DATABASE;

    public static final String VIEW = "/seng202.team4";
    public static final String USER_INTERFACES = "/user_interfaces";
    public static final String TEST_DATA = "/test_data";
    public static final String MAIN_SCENE_FXML = VIEW + USER_INTERFACES + "/mainFlightTrackerScene.fxml";
    public static final String HOME_SCENE_FXML = VIEW + USER_INTERFACES + "/homePage.fxml";
    public static final String AIRLINE_SCENE_FXML = VIEW + USER_INTERFACES + "/airlineTab.fxml";
    public static final String AIRPORT_SCENE_FXML = VIEW + USER_INTERFACES + "/airportTab.fxml";
    public static final String ROUTE_SCENE_FXML = VIEW + USER_INTERFACES + "/routeTab.fxml";
    public static final String MAP_SCENE_FXML = VIEW + USER_INTERFACES + "/mapTab.fxml";
    public static final String EMISSIONS_SCENE_FXML = VIEW + USER_INTERFACES + "/emissionsTab.fxml";
    public static final String NEW_AIRLINE_FXML = VIEW + USER_INTERFACES + "/newAirline.fxml";
    public static final String NEW_AIRPORT_FXML = VIEW + USER_INTERFACES + "/newAirport.fxml";
    public static final String NEW_ROUTE_FXML = VIEW + USER_INTERFACES + "/newRoute.fxml";
    public static final String NEW_FLIGHT_PATH_FXML = VIEW + USER_INTERFACES + "/newFlightPath.fxml";
    public static final String UPLOAD_SCENE_FXML = VIEW + USER_INTERFACES + "/fileUpload.fxml";
    public static final String INVALID_LINES_POPUP_FXML = VIEW + USER_INTERFACES + "/invalidLinesPopUp.fxml";
    public static final String AIRLINE_DETAILS = VIEW + USER_INTERFACES + "/airlineDetails.fxml";
    public static final String AIRPORT_DETAILS = VIEW + USER_INTERFACES + "/airportDetails.fxml";
    public static final String ROUTE_DETAILS = VIEW + USER_INTERFACES + "/routeDetails.fxml";
    public static final String FLIGHTPATH_DETAILS = VIEW + USER_INTERFACES + "/flightPathDetails.fxml";
    public static final String EMISSIONS_DETAILS = VIEW + USER_INTERFACES + "/emissionsDetails.fxml";
    public static final String CONTRIBUTIONS_GRAPH = VIEW + USER_INTERFACES + "/emissionsContributionsGraph.fxml";
    public static final String ERROR_FXML = VIEW + USER_INTERFACES + "/error.fxml";

    public static final String REFRESH_BUTTON_PNG = VIEW + "/images/refresh_icon.png";
    public static final String ADD_RECORD_BUTTON_PNG = VIEW + "/images/add_record_image.png";
    public static final String DELETE_RECORD_BUTTON_PNG = VIEW + "/images/delete_record_image.png";
    public static final String APP_ICON = VIEW + "/images/logo_icon.png";
    public static final String INFO_ICON = VIEW + "/images/info_icon.png";
    public static final String FLIGHTPATH_TYPE = VIEW + "/images/flight_path_type.PNG";
    public static final String AIRLINE_FORMAT = VIEW + "/images/airline_format.PNG";
    public static final String AIRPORT_FORMAT = VIEW + "/images/airport_format.PNG";
    public static final String ROUTE_FORMAT = VIEW + "/images/route_format.PNG";


    public static final String STYLE_SHEET = VIEW + USER_INTERFACES + "/styles.css";

    public static final String AIRLINE_RSC = "/airlines.dat";
    public static final String AIRPORT_RSC = "/airports.dat";
    public static final String ROUTE_RSC = "/routes.dat";
    public static final String FLIGHT_PATH_RSC = "/NZCH-WSSS.csv";
    public static final String USER_MANUAL = "/greenflights_user_manual.pdf";
    public static final String MAP_RSC = "/seng202.team4/user_interfaces/map.html";
    public static final String AIRLINE_TEST_RSC_VALID = "/airlineValid.dat";
    public static final String AIRLINE_TEST_RSC_RECORD = "/airlineRecord.dat";
    public static final String AIRLINE_TEST_RSC_DELETE = "/airlineDelete.dat";
    public static final String AIRLINE_TEST_RSC_UPDATE = "/airlineUpdate.dat";
    public static final String AIRLINE_TEST_RSC_INVALID = "/airlineInvalid.dat";
    public static final String AIRPORT_TEST_RSC_VALID = "/airportValid.dat";

}
