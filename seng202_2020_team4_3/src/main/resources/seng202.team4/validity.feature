Feature: Validity

#adding airline records
  Scenario: Adding a new airline record with valid name
    Given I am adding new airline data
    When I enter "Dynamite" airline name
    Then airline should be valid

  Scenario: Adding a new airline with invalid name
    Given I am adding new airline data
    When I enter "875~" airline name
    Then airline should not be valid

  Scenario: Adding a new airline record with valid IATA
    Given I am adding new airline data
    When I enter "BTS" airline IATA
    Then airline should be valid

  Scenario: Adding a new airline record with invalid IATA
    Given I am adding new airline data
    When I enter "ANTI⁷" airline IATA
    Then airline should not be valid

  Scenario: Adding a new airline record with valid ICAO
    Given I am adding new airline data
    When I enter "TXT" airline ICAO
    Then airline should be valid

  Scenario: Adding a new airline record with invalid ICAO
    Given I am adding new airline data
    When I enter "RUN9¾" airline ICAO
    Then airline should not be valid

  Scenario: Adding a new airline record with valid country
    Given I am adding new airline data
    When I enter "Magic Island" airline country
    Then airline should be valid

  Scenario: Adding a new airline record with invalid country
    Given I am adding new airline data
    When I enter "134340~" airline country
    Then airline should not be valid

#adding airport records
  Scenario: Adding a new airport record with valid name
    Given I am adding new airport data
    When I enter "BTS" airport name
    Then airport should be valid

  Scenario: Adding a new airport record with invalid name
    Given I am adding new airport data
    When I enter "ANTI⁷~" airport name
    Then airport should not be valid

  Scenario: Adding a new airport record with valid IATA
    Given I am adding new airport data
    When I enter "BTS" airport IATA
    Then airport should be valid

  Scenario: Adding a new airport record with invalid IATA
    Given I am adding new airport data
    When I enter "ANTI⁷" airport IATA
    Then airport should not be valid

  Scenario: Adding a new airport record with valid latitude
    Given I am adding new airport data
    When I enter "7.0613" airport latitude
    Then airport should be valid

  Scenario: Adding a new airport record with invalid latitude
    Given I am adding new airport data
    When I enter "forty" airport latitude
    Then airport should not be valid

  Scenario: Adding a new airport record with valid timezone
    Given I am adding new airport data
    When I enter "7" airport timezone
    Then airport should be valid

  Scenario: Adding a new airport record with invalid timezone
    Given I am adding new airport data
    When I enter "0 o'clock" airport timezone
    Then airport should not be valid

  Scenario: Adding a new airport record with valid DST
    Given I am adding new airport data
    When I enter "U" airport DST
    Then airport should be valid

  Scenario: Adding a new airport record with invalid DST
    Given I am adding new airport data
    When I enter "X" airport DST
    Then airport should not be valid

  Scenario: Adding a new airport record with valid Tz database time
    Given I am adding new airport data
    When I enter "America/Thule" airport Tz database time
    Then airport should be valid

  Scenario: Adding a new airport record with invalid Tz database time
    Given I am adding new airport data
    When I enter "New Zealand" airport Tz database time
    Then airport should not be valid

  #adding route records
  Scenario: Adding a new route record with valid airline
    Given I am adding new route data
    When I enter "6X" route airline
    Then route should be valid

  Scenario: Adding a new route record with invalid airline
    Given I am adding new route data
    When I enter "T⁷" route airline
    Then route should not be valid

  Scenario: Adding a new route record with valid source
    Given I am adding new route data
    When I enter "ATZ" route source
    Then route should be valid

  Scenario: Adding a new route record with invalid source
    Given I am adding new route data
    When I enter "T⁷" route source
    Then route should not be valid

  Scenario: Adding a new route record with valid destination
    Given I am adding new route data
    When I enter "ATZ" route destination
    Then route should be valid

  Scenario: Adding a new route record with invalid destination
    Given I am adding new route data
    When I enter "T⁷" route destination
    Then route should not be valid

  Scenario: Adding a new route record with valid code share
    Given I am adding new route data
    When I enter "Y" route code share
    Then route should be valid

  Scenario: Adding a new route record with invalid code share
    Given I am adding new route data
    When I enter "N" route code share
    Then route should not be valid

  Scenario: Adding a new route record with valid stops
    Given I am adding new route data
    When I enter "3" route stops
    Then route should be valid

  Scenario: Adding a new route record with invalid stops
    Given I am adding new route data
    When I enter "twenty" route stops
    Then route should not be valid