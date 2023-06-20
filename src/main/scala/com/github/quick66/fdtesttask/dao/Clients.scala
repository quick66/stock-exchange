package com.github.quick66.fdtesttask.dao

import com.github.quick66.fdtesttask.entities.{BuyOrder, Client, Papers, SellOrder}

class Clients(underlyingMap: Map[String, Client]) {

  def applySale(sell: SellOrder, buy: BuyOrder): Unit = {
    if (sell.client != buy.client) {
      underlyingMap(sell.client).sell(sell)
      underlyingMap(buy.client).buy(buy)
    }
  }

  override def toString: String = {
    underlyingMap.values.map { c =>
      val amountsStr = Papers.list.map(c.amounts).mkString("\t")
      s"${c.id}\t${c.balance}\t$amountsStr"
    }.mkString("\n")
  }

}
