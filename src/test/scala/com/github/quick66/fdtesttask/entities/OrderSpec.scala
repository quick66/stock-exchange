package com.github.quick66.fdtesttask.entities

import org.scalatest.flatspec.AnyFlatSpec

class OrderSpec extends AnyFlatSpec {

  "BuyOrder" should "be parsed from string tokens of proper format" in {
    val order = BuyOrder("C1", OrderParams("A", 5, 100))
    val orderStr = "C1\tb\tA\t5\t100"
    assertResult(order)(BuyOrder.parse(orderStr).get)
  }

  "SellOrder" should "be parsed from string tokens of proper format" in {
    val order = SellOrder("C1", OrderParams("A", 5, 100))
    val orderStr = "C1\ts\tA\t5\t100"
    assertResult(order)(SellOrder.parse(orderStr).get)
  }

}
