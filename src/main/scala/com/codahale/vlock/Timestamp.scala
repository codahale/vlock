package com.codahale.vlock

object Timestamp {
  def apply(): Timestamp = Timestamp(0)
}

case class Timestamp(time: Long) extends Ordered[Timestamp] {
  def compare(that: Timestamp) = time.compare(that.time)
  def max(that: Timestamp) = if (this < that) that else this
  override def toString = "%016x".format(time)
}
