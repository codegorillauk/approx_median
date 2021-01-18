package com.codegorillauk.algo

class NormalMedian extends Median {
  private var intermediate = Vector.newBuilder[Double]

  override def add(value: Double): Unit = intermediate += value

  override def calculate(): Double = {
    val ordered = intermediate.result().sorted
    if (ordered.length % 2 != 0) {
      ordered(ordered.length / 2)
    } else {
      (ordered((ordered.length - 1) / 2) + ordered(ordered.length / 2)) / 2.0
    }
  }
}


