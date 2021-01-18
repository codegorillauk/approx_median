# approx_median
 
This aims to demonstrate a technique for performing an approximate median calculation on big data sets with fixed memory costs.
 
If you know the min and max value of the input data set (or at least an estmation of where the median would be within a range) then you can configure the median calculation to either precisly calculate the median or, if the range of values is very large, an approximation of where the median value would be.
 
This example also demonstrates the normal way of calculated a median (working on a sorted collection) as a comparison.
 
This technique can also be used to create a "storage" model of the current state of the median calculation, that can be used in merge scenarios to create a distributed median calculation, for distributed compute engines such as Apache Spark or in cases where you need to store the progress of an aggregation for further aggregations in the future (reaggregatable data).
 
As an example, 
 
We have input data that is the success rate of mobile phone connections, currently aggregated on 1 minute intervals.

There are 60m rows per hour.

We want to be able to calculate the median success rate, per minute, per hour, per day, per week.

Success rates are in the range of 0% to 100%, and down to 2 decimal places.

For a normal median, and a precise approx_median calculation

| period        | input rows     | median (storage) | approx_median (storage) |
| ------------- |---------------:|-----------------:|------------------------:|
| 1 minute      | 1,000,000      | 1,000,000        | 10,000                  |
| 1 hour        | 60,000,000     | 60,000,000       | 10,000                  |
| 1 day         | 1,440,000,000  | 1,440,000,000    | 10,000                  |
| 1 week        | 10,080,000,000 | 10,080,000,000   | 10,000                  |

For a normal median all the data must be within a set to find the mid value.

The approx_median creates a histogram of each possible value, hence the min (0.00) and max (100.00) values, along with the step (0.01) are required to build the full histogram. From this histogram the median value can be calculated by finding the mid "count". If the range of values includes every possible value of the input field then the approx_median is 100% accurate, as you configure these to reduce the storage size then the median is less than 100% accurate.

To allow for parallel caculations (especially useful in distributed systems), the input data would be chunked into smaller compute units that each thread would work on. The approx median allows this to be chunked up into smaller units, say 1 million rows per chunk, each of these generates a 10,000 counter array. when collecting these together they can be merged by adding each matching bin in the two histograms to generate the merged histogram. Continue to do this until there is only one historgram remaining and that can be used to calculate the median of the original dataset.
