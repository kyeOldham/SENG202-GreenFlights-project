Feature: Calculations

  Scenario: Calculate carbon emissions based on a distance of 100 m.
    Given there is a new Route
    When I set the distance to 100.0
    Then the calculated carbon emissions should be 11.5
    And the dollar offset should be 0.17
    And the trees equivalent should be 1

  Scenario: Calculate carbon emissions based on a distance of 750 m.
    Given there is a new Route
    When I set the distance to 750.0
    Then the calculated carbon emissions should be 86.25
    And the dollar offset should be 1.27
    And the trees equivalent should be 2

  Scenario: Calculate carbon emissions based on a distance of 0 m.
    Given there is a new Route
    When I set the distance to 0.0
    Then the calculated carbon emissions should be 0.0
    And the dollar offset should be 0.0
    And the trees equivalent should be 0
