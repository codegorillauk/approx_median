package com.codegorillauk.algo


class ApproxMedian(min: Int, Max: Int, step: Int) extends Median {
  // cnt  2    1     2     1
  //
  //     0-9 10-19 20-29 30-39 ......
  // bin  0    1     2     3

  private val histogram = new Array[Int]((Max+step - min) / step)
  //println(s"Bins = ${histogram.length}")
  private var total = 0L

  override def add(value: Double): Unit = {
    val bin = ((value - min) / step).toInt
    //println(s"Adding $value to $bin")
    histogram(bin) += 1
    total += 1
  }

  /*private def findElement(find: Long, startIndex : Int = 0) = {
    var element = find
    var offset = 0
    val remaining =
      histogram.drop(startIndex).zipWithIndex.dropWhile {
      case (count, index) if count >= element =>
        // found
        false
      case (count, _) =>
        element -= count
        offset += 1
        true
    }

    (offset + startIndex) -> element
  }*/

  private def findElement(find: Long, startIndex : Int = 0) = {
    // not the scala way - but is faster
    var countDown = find
    var index = startIndex
    while(histogram(index) < countDown ) {
      countDown -= histogram(index)
      index += 1
    }
    index -> countDown
  }



  private def binToValue(bin : Int) : Double = {
    val lowerBound = (bin * step) + min
    val result = lowerBound
    //println(s"matched bin = $bin value = $result")
    result
  }

  override def calculate(): Double = {
    //println(histogram.mkString("Array(", ", ", ")"))

    //val total = histogram.sum

    if (total % 2 != 0) {
      val element = (total + 1) / 2 // one based
      val (binIndex,_) = findElement(element)
      binToValue(binIndex)
    } else {
      var element = total / 2
      val (binIndex, insideBin) = findElement(element)
      val countInBin = histogram(binIndex)
      if(countInBin > insideBin) {
        binToValue(binIndex)
      } else {
        val (nextBin, _) = findElement(1, binIndex+1)
        (binToValue(binIndex) + binToValue(nextBin)) / 2.0
      }
    }
  }
}

