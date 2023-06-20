package com.github.quick66.fdtesttask.entities

import scala.collection.mutable

case class Client(
  id: String,
  var balance: Int,
  amounts: mutable.Map[String, Int]
) {

  def buy(order: BuyOrder): Client = {
    balance = balance - order.params.price * order.params.amount
    amounts(order.params.paper) = amounts(order.params.paper) + order.params.amount
    this
  }

  def sell(order: SellOrder): Client = {
    balance = balance + order.params.price * order.params.amount
    amounts(order.params.paper) = amounts(order.params.paper) - order.params.amount
    this
  }

}

object Client {

  def parse(str: String): Option[Client] = {
    str.split("\t") match {
      case a if a.length > 2 =>
        val amounts = mutable.Map.from(Papers.list zip a.drop(2).map(_.toInt))
        Some(Client(a(0), a(1).toInt, amounts))
    }
  }

}
