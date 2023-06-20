package com.github.quick66.fdtesttask

import com.github.quick66.fdtesttask.dao.{Clients, Orders}
import com.github.quick66.fdtesttask.entities.{BuyOrder, Client, SellOrder}
import com.github.quick66.fdtesttask.utils.FileIO

object Main extends App {

  val clients = {
    val mapBuilder = Map.newBuilder[String, Client]
    FileIO.readFile("clients.txt")
      .flatMap(Client.parse)
      .foreach { c => mapBuilder += c.id -> c }
    new Clients(mapBuilder.result())
  }

  println(clients)
  println("---BEGIN---")

  val sellOrders = new Orders[SellOrder]
  val buyOrders = new Orders[BuyOrder]

  FileIO.readFile("orders.txt")
    .flatMap(l => BuyOrder.parse(l) orElse SellOrder.parse(l))
    .foreach {
      case b@BuyOrder(_, params) => sellOrders.pop(params, clients.applySale(_, b), buyOrders.put(params, b))
      case s@SellOrder(_, params) => buyOrders.pop(params, clients.applySale(s, _), sellOrders.put(params, s))
    }

  println(clients)
  FileIO.printFile("result.txt", clients)

}
