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

  "Orders" should "pop first order in queue of any other client if exists" in {
    val goodOrderParams = OrderParams("A", 24, 42)
    val so1 = SellOrder("C1", goodOrderParams)
    val paramsNotFound = OrderParams("B", 12, 7)
    val ordersMap = mutable.Map(
      goodOrderParams -> mutable.Queue(so1, SellOrder("C2", goodOrderParams))
    )
    val orders = new Orders(ordersMap)

    // C3 захотел купить с подходящими параметрами - получает предложение от C1
    orders.pop(goodOrderParams, "C3", assertResult(so1), fail("Order must be found!"))
    // C3 захотел купить с параметрами, которых нет в списке продаж - ничего не получает
    orders.pop(paramsNotFound, "C3", _ => fail("Order with this params must not be found!"), assert(true))
    // C2 захотел купить с подходящими параметрами, но в списке уже есть его предложение - ничего не получает
    orders.pop(goodOrderParams, "C2", _ => fail("Order for self-buy must not be found!"), assert(true))


    // Добавляем предложение от C3 и C2 теперь может купить
    val so3 = SellOrder("C3", goodOrderParams)
    ordersMap(goodOrderParams).enqueue(so3)
    orders.pop(goodOrderParams, "C2", assertResult(so3), fail("Order must be found!"))
  }

}
