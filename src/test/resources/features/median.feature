Feature: Approx Median
  In order to calculate the median of a large dataset
  As a data scientist
  I want a median that can scale

  Background:

  Scenario Outline: Calculating a median
    #Given a data set from <start> to <stop>
    Given a random data set from <start> to <stop> of <elements> elements
    When I calculate a true median
    #Then the median is <median>
    When I calculate an approx median with range <start> to <stop> and step <step>
    When I calculate a true median
    When I calculate an approx median with range <start> to <stop> and step <step>
    #Then the approx median is <approx>
    Then the approx median is the same as the real median
    Examples:
      | start | stop | median | approx | step | elements | comments |
#      | 1     | 1000 | 500    | 500    | 1    | 1        | worst case |
      | 1     | 100000 | 500    | 500    | 1    | 1800     | 1600 performance time equality (was 5000) |
#      | 1     | 10000 | 500.5  | 500.5  | 1    | 1000000   | lots    |
#      | 1     | 10000 | 500.5  | 500.5  | 1    | 10000000   | lots    |
#      | 1     | 10000 | 500.5  | 500.5  | 1    | 100000000   | lots    |
      #| 1     | 1000 | 500    | 500    | 1    | 99999999     |


