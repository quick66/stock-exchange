package com.github.quick66.fdtesttask.entities

case class OrderParams(
  paper: String,
  price: Int,
  amount: Int
)

sealed trait Order {
  def client: String

  def params: OrderParams
}

case class BuyOrder(client: String, params: OrderParams) extends Order

object BuyOrder {

  def parse(str: String): Option[BuyOrder] = {
    str.split("\t") match {
      case Array(client, "b", paper, price, amount) =>
        Some(BuyOrder(client, OrderParams(paper, price.toInt, amount.toInt)))
      case _ =>
        None
    }
  }

}

case class SellOrder(client: String, params: OrderParams) extends Order

object SellOrder {

  def parse(str: String): Option[SellOrder] = {
    str.split("\t") match {
      case Array(client, "s", paper, price, amount) =>
        Some(SellOrder(client, OrderParams(paper, price.toInt, amount.toInt)))
      case _ =>
        None
    }
  }

}
