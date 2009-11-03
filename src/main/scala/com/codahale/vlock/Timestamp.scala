package com.codahale.vlock

object Timestamp {
  def apply(): Timestamp = Timestamp(0)
}

case class Timestamp(time: Long) extends Ordered[Timestamp] {
  def compare(that: Timestamp) = time.compare(that.time)
  override def toString = "%016x".format(time)
}
