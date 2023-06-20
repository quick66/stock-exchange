package com.github.quick66.fdtesttask.entities

import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class ClientSpec extends AnyFlatSpec {

  "Client" should "be parsed from string tokens of proper format" in {
    val client = Client("C1", 42, mutable.Map("A" -> 100, "B" -> 500, "C" -> 88, "D" -> 41))
    val clientStr = "C1\t42\t100\t500\t88\t41"
    assertResult(client)(Client.parse(clientStr).get)
  }

  "Client" should "increment amount and decrement balance when buying" in {
    val client = Client("C1", 4242, mutable.Map("A" -> 500))
    val order = BuyOrder("C1", OrderParams("A", 5, 100))
    client.buy(order)
    assertResult(3742)(client.balance)
    assertResult(600)(client.amounts("A"))
  }

  "Client" should "decrement amount and increment balance when buying" in {
    val client = Client("C1", 4242, mutable.Map("A" -> 500))
    val order = SellOrder("C1", OrderParams("A", 5, 100))
    client.sell(order)
    assertResult(4742)(client.balance)
    assertResult(400)(client.amounts("A"))
  }

}
