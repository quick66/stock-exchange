package com.github.quick66.fdtesttask.exchange

import com.github.quick66.fdtesttask.dao.{Clients, Orders}
import com.github.quick66.fdtesttask.entities.{BuyOrder, Order, SellOrder}

object Exchange {

  def process(clients: Clients, orders: Seq[Order]): Unit = {
    val sellOrders = new Orders[SellOrder]
    val buyOrders = new Orders[BuyOrder]

    orders.foreach {
      case b@BuyOrder(c, p) => sellOrders.pop(p, c, clients.applySale(_, b), buyOrders.put(p, b))
      case s@SellOrder(c, p) => buyOrders.pop(p, c, clients.applySale(s, _), sellOrders.put(p, s))
    }
  }

}
