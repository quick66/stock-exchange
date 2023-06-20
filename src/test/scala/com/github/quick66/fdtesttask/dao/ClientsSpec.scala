package com.github.quick66.fdtesttask.dao

import com.github.quick66.fdtesttask.entities.{BuyOrder, Client, OrderParams, SellOrder}
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class ClientsSpec extends AnyFlatSpec {

  "Clients" should "apply sale for matching clients" in {
    val c1 = Client("C1", 42, mutable.Map("A" -> 100500))
    val c2 = Client("C2", 4242, mutable.Map("A" -> 100))
    val clients = new Clients(Map(c1.id -> c1, c2.id -> c2))
    val sellOrder = SellOrder("C1", OrderParams("A", 5, 500))
    val buyOrder = BuyOrder("C2", OrderParams("A", 5, 500))
    clients.applySale(sellOrder, buyOrder)
    assertResult(100000)(c1.amounts("A"))
    assertResult(2542)(c1.balance)
    assertResult(600)(c2.amounts("A"))
    assertResult(1742)(c2.balance)
  }

  "Clients" should "not apply sale for same client" in {
    val c1 = Client("C1", 42, mutable.Map("A" -> 100500))
    val clients = new Clients(Map(c1.id -> c1))
    val sellOrder = SellOrder("C1", OrderParams("A", 5, 500))
    val buyOrder = BuyOrder("C1", OrderParams("A", 5, 500))
    clients.applySale(sellOrder, buyOrder)
    assertResult(100500)(c1.amounts("A"))
    assertResult(42)(c1.balance)
  }

  "Clients" should "convert to string in proper format" in {
    val c1 = Client("C1", 42, mutable.Map("A" -> 100, "B" -> 500, "C" -> 88, "D" -> 41))
    val clientsStr = "C1\t42\t100\t500\t88\t41"
    val clients = new Clients(Map(c1.id -> c1))
    assertResult(clientsStr)(clients.toString)
  }

}
