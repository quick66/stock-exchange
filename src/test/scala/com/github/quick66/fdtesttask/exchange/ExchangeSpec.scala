package com.github.quick66.fdtesttask.exchange

import com.github.quick66.fdtesttask.dao.Clients
import com.github.quick66.fdtesttask.entities.{BuyOrder, Client, OrderParams, Papers, SellOrder}
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class ExchangeSpec extends AnyFlatSpec {

  "Exchange" should "process clients orders as they occur in input" in {
    val c4 = Client("C4", 560, Papers.list zip Seq(450, 540, 480, 950) to mutable.Map)
    val c6 = Client("C6", 1300, Papers.list zip Seq(890, 320, 100, 0) to mutable.Map)
    val c8 = Client("C8", 7000, Papers.list zip Seq(90, 190, 0, 0) to mutable.Map)
    val clients = new Clients(Seq(c4, c6, c8).map(c => c.id -> c).toMap)
    val orders = Seq(
      BuyOrder(c8.id, OrderParams("A", 12, 2)),
      SellOrder(c4.id, OrderParams("A", 12, 2)),
      SellOrder(c6.id, OrderParams("A", 11, 4)),
      SellOrder(c4.id, OrderParams("A", 10, 3)),
      BuyOrder(c6.id, OrderParams("A", 11, 4)),
      SellOrder(c4.id, OrderParams("A", 11, 4))
    )
    Exchange.process(clients, orders)
    assertResult(628)(c4.balance)
    assertResult(444)(c4.amounts("A"))
    assertResult(1256)(c6.balance)
    assertResult(894)(c6.amounts("A"))
    assertResult(6976)(c8.balance)
    assertResult(92)(c8.amounts("A"))
  }

}
