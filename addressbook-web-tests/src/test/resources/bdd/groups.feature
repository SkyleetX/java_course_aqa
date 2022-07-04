Feature: Groups

  Scenario Outline: Group creation
    Given a set of groups
    When I create a new group with name <name>, header <header> and footer <footer>
    Then the new set of groups is equal to the old set of groups with added group

    Examples:
      | name         | header         | footer         |
      | test1 name   | test1 header   | test1 footer   |
      | test2 name   | test2 header   | test2 footer   |
