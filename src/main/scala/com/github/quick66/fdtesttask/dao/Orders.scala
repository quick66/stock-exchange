package com.github.quick66.fdtesttask.dao

import com.github.quick66.fdtesttask.entities.{Order, OrderParams}

import scala.collection.mutable

class Orders[T <: Order](underlying: mutable.Map[OrderParams, mutable.Queue[T]] = mutable.Map.empty[OrderParams, mutable.Queue[T]]) {

  def pop(params: OrderParams, client: String, onSuccess: T => Unit, onNotFound: => Unit): Unit = {
    underlying.get(params) match {
      case Some(orders) if orders.nonEmpty =>
        orders.dequeueFirst(_.client != client).map(onSuccess).getOrElse(onNotFound)
      case _ =>
        onNotFound
    }
  }

  def put(params: OrderParams, order: T): Unit = {
    underlying.getOrElseUpdate(params, mutable.Queue.empty).enqueue(order)
  }

}
