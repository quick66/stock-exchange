package com.github.quick66.fdtesttask.dao

import com.github.quick66.fdtesttask.entities.{OrderParams, SellOrder}
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.mutable

class OrdersSpec extends AnyFlatSpec {

  "Orders" should "store orders with same params in one queue" in {
    val op1 = OrderParams("A", 24, 42)
    val op2 = OrderParams("B", 12, 7)
    val op3 = OrderParams("B", 1, 27)
    val op4 = OrderParams("C", 12, 52)
    val ordersMap = mutable.Map(
      op1 -> mutable.Queue(SellOrder("C1", op1)),
      op2 -> mutable.Queue(SellOrder("C1", op2)),
      op3 -> mutable.Queue(SellOrder("C2", op3))
    )
    val orders = new Orders(ordersMap)

    orders.put(op1, SellOrder("C2", op1))
    assertResult(2)(ordersMap(op1).size)
    assertResult(1)(ordersMap(op2).size)
    assertResult(1)(ordersMap(op3).size)

    orders.put(op2, SellOrder("C2", op2))
    assertResult(2)(ordersMap(op1).size)
    assertResult(2)(ordersMap(op2).size)
    assertResult(1)(ordersMap(op3).size)

    orders.put(op3, SellOrder("C1", op3))
    assertResult(2)(ordersMap(op1).size)
    assertResult(2)(ordersMap(op2).size)
    assertResult(2)(ordersMap(op3).size)

    orders.put(op4, SellOrder("C1", op4))
    assertResult(2)(ordersMap(op1).size)
    assertResult(2)(ordersMap(op2).size)
    assertResult(2)(ordersMap(op3).size)
    assertResult(1)(ordersMap(op4).size)
  }

  "Orders" should "pop first order in queue if exists" in {
    val op1 = OrderParams("A", 24, 42)
    val so1 = SellOrder("C1", op1)
    val op2 = OrderParams("B", 12, 7)
    val ordersMap = mutable.Map(
      op1 -> mutable.Queue(so1, SellOrder("C2", op1))
    )
    val orders = new Orders(ordersMap)

    orders.pop(op1, assertResult(so1), fail("Order must be found!"))
    orders.pop(op2, _ => fail("Order must not be found!"), assert(true))
  }

}
