import com.codegorillauk.algo.{ApproxMedian, Median, NormalMedian}
import io.cucumber.scala.{EN, ScalaDsl}
import org.scalatest.matchers.should.Matchers

class Steps extends ScalaDsl with EN with Matchers {

  private var testData: Seq[Double] = Seq.empty
  private var trueMedian = Double.MinValue
  private var approxMedian = Double.MinValue

  def time[T](context : String, print : Boolean = true)(func: => T): T = {
    val start = System.nanoTime()
    val result = func
    val elapsed = System.nanoTime() - start
    if(print) {
      println(s"Elapsed time for $context: $elapsed ns (${elapsed / 1E9} secs)")
    }
    result
  }

  def test(context:String, algoBuilder: => Median) : Double = {
    val print = false
    time(context){
      val algo = time(context + "_build",print) {algoBuilder}
      time(context + "_add",print) {testData.foreach(algo.add)}
      time(context + "_calc",print) {algo.calculate()}
    }
  }

  Given("""a data set from {int} to {int}""") { (start: Int, end: Int) =>
    testData = start to end map(_.toDouble)
  }

  Given("""a random data set from {int} to {int} of {int} elements""") { (start: Int, end: Int, elements: Int) =>
    val rng = new scala.util.Random()
    testData = 1 to elements map(_ => rng.nextInt(end-start) + start) map(_.toDouble)
    //println(testData.sorted.mkString("Input(", ", ", ")"))
  }

  When("""I calculate a true median""") { () =>
    trueMedian = test("NormalMedian", new NormalMedian)
  }

  Then("""the median is {double}""") { (expectedMedian: Double) =>
    trueMedian should be(expectedMedian)
  }

  When("""I calculate an approx median with range {int} to {int} and step {int}""") {
    (start: Int, end: Int, step: Int) =>
      approxMedian = test("ApproxMedian", new ApproxMedian(start, end, step))
  }

  Then("""the approx median is {double}""") { (expectedMedian: Double) =>
    approxMedian should be(expectedMedian)
  }

  Then("""the approx median is the same as the real median""") { () =>
    approxMedian should be(trueMedian)
  }
}
