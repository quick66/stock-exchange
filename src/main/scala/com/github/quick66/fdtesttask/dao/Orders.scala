package com.github.quick66.fdtesttask.dao

import com.github.quick66.fdtesttask.entities.{Order, OrderParams}

import scala.collection.mutable

class Orders[T <: Order](underlying: mutable.Map[OrderParams, mutable.Queue[T]] = mutable.Map.empty[OrderParams, mutable.Queue[T]]) {

  def pop(params: OrderParams, onSuccess: T => Unit, onNotFound: => Unit): Unit = {
    underlying.get(params) match {
      case Some(sales) if sales.nonEmpty => onSuccess(sales.dequeue())
      case _ => onNotFound
    }
  }

  def put(params: OrderParams, order: T): Unit = {
    underlying.getOrElseUpdate(params, mutable.Queue.empty).enqueue(order)
  }

}
