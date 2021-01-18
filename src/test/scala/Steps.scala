import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.matchers.should.Matchers

class ApproxMedian(min: Int, Max: Int, step: Int) {
  private val histogram = Array.fill((Max+step - min) / step)(0)
  println(s"Bins = ${histogram.length}")

  def add(value: Int): Unit = {
    val bin = (value - min) / step
    histogram(bin) += 1
  }

  private def findElement(find: Int) = {
    var element = find
    val remaining = histogram.zipWithIndex.dropWhile {
      case (count, index) if count >= element =>
        // found
        false
      case (count, _) =>
        element -= count
        true
    }

    remaining -> element
  }

  private def binToValue(bin : Int) : Double = {
    val midStep = step/2.0
    val lowerBound = (bin * step) + min
    //val upperBound = ((bin + 1) * step) + min
    val result = lowerBound //(lowerBound + upperBound) / 2.0
    println(s"matched bin = $bin value = $result")
    result
  }

  def calculate(): Double = {
    println(histogram.mkString("Array(", ", ", ")"))

    val total = histogram.sum

    // cnt  2    1     2     1
    //
    //      1          1
    //      1    1     1     1
    //     0-9 10-19 20-29 30-39 ......
    // bin  0    1     2     3

    if (total % 2 != 0) {
      val element = (total + 1) / 2 // one based
      binToValue(findElement(element)._1.head._2)
    } else {
      var element = total / 2
      val remaining = findElement(element)
      val firstBin = remaining._1.head._2
      val insideBin = remaining._2
      val inBin = remaining._1.head._1
      if(inBin > insideBin) {
        binToValue(firstBin)
      } else {
        (binToValue(firstBin) + binToValue(firstBin+1)) / 2.0
      }
    }
  }
}

class Steps extends ScalaDsl with EN with Matchers {

  private var testData: Seq[Int] = Seq.empty
  private var trueMedian = Double.MinValue
  private var trueMedianTime = 0L
  private var approxMedian = Double.MinValue
  private var approxMedianTime = 0L

  Given("""a data set from {int} to {int}""") { (start: Int, end: Int) =>
    testData = start to end
  }

  Given("""a random data set from {int} to {int} of {int} elements""") { (start: Int, end: Int, elements: Int) =>
    scala.util.Random.nextInt(end-start) + start
    testData = (1 to elements).map(_ => scala.util.Random.nextInt(end-start) + start)
  }

  When("""I calculate a true median""") { () =>
    val startTime = System.nanoTime()
    val ordered = testData.sorted
    if (testData.length % 2 != 0) {
      trueMedian = ordered(testData.length / 2)
    } else {
      trueMedian = (ordered((testData.length - 1) / 2) + ordered(testData.length / 2)) / 2.0
    }
    trueMedianTime = System.nanoTime() - startTime
  }

  Then("""the median is {double}""") { (expectedMedian: Double) =>
    trueMedian should be(expectedMedian)
  }

  When("""I calculate an approx median with range {int} to {int} and step {int}""") {
    (start: Int, end: Int, step: Int) =>
      val startTime = System.nanoTime()
      val approx = new ApproxMedian(start,end,step)
      testData.foreach(approx.add)
      approxMedian = approx.calculate()
      approxMedianTime = System.nanoTime() - startTime
  }

  Then("""the approx median is {double}""") { (expectedMedian: Double) =>
    approxMedian should be(expectedMedian)
  }

  Then("""the approx median is the same as the real median""") { () =>
    println(s"actual median = ${trueMedian} in ${trueMedianTime /1E9} - approx median = ${approxMedian} in ${approxMedianTime /1E9}")
    approxMedian should be(trueMedian)
  }
}
