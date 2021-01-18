Feature: Approx Median
  In order to calcualte the median of a large dataset
  As a data scientist
  I want a median that can scale

  Background:

  Scenario Outline: Calculating a median
    Given a random data set from <start> to <stop> of <elements> elements
    When I calculate a true median
    #Then the median is <median>
    When I calculate an approx median with range <start> to <stop> and step <step>
    #Then the approx median is <approx>
    Then the approx median is the same as the real median
    Examples:
      | start | stop | median | approx | step | elements |
      | 1     | 1000 | 500.5  | 500.5  | 1    | 100000000    |
      | 1     | 1000 | 500    | 500    | 1    | 99999999     |

