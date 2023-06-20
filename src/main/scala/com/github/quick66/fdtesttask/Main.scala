package com.github.quick66.fdtesttask

import com.github.quick66.fdtesttask.dao.Clients
import com.github.quick66.fdtesttask.entities.{BuyOrder, Client, SellOrder}
import com.github.quick66.fdtesttask.exchange.Exchange
import com.github.quick66.fdtesttask.utils.FileIO

object Main extends App {

  val clients = {
    val mapBuilder = Map.newBuilder[String, Client]
    FileIO.readFile("clients.txt")
      .flatMap(Client.parse)
      .foreach { c => mapBuilder += c.id -> c }
    new Clients(mapBuilder.result())
  }

  val orders = FileIO.readFile("orders.txt")
    .flatMap(l => BuyOrder.parse(l) orElse SellOrder.parse(l))

  println("---DEBUG CLIENTS---")
  println(clients)

  Exchange.process(clients, orders)

  println("---DEBUG RESULT---")
  println(clients)
  FileIO.printFile("result.txt", clients)

}
