package com.codahale.vlock

import scala.collection.mutable.{Map => MMap}

object VectorClock extends LamportClock {
  def apply(): VectorClock = VectorClock(this, Map())
}

case class VectorClock(clock: Clock, versions: Map[Node, Timestamp])
             extends PartiallyOrdered[VectorClock] {
  
  def tryCompareTo [B >: VectorClock <% PartiallyOrdered[B]](b: B): Option[Int] = {
    b match {
      case VectorClock(clock, that) =>
        if (lessThan(versions, that))      Some(-1)
        else if (lessThan(that, versions)) Some( 1)
        else if (versions == that)         Some( 0)
        else                               None
      case _ => None
    }
  }
  
  def ==(that: VectorClock) = versions == that.versions
  
  /**
   * Returns true if <code>this</code> and <code>that</code> are concurrent.
   */
  def <>(that: VectorClock) = tryCompareTo(that) == None
  
  def +(node: Node) = VectorClock(clock, versions + (node -> clock.timestamp))
  
  def merge(that: VectorClock): VectorClock = VectorClock(clock, merge(versions, that.versions))
  
  private def merge(a: Map[Node, Timestamp], b: Map[Node, Timestamp]): Map[Node, Timestamp] = {
    def max(x: Timestamp, y: Timestamp) = if (x < y) y else x
    val c = MMap[Node, Timestamp]() ++ b
    for ((n, t) <- a) {
      c(n) = max(c.getOrElse(n, t), t)
    }
    return Map() ++ c
  }
  
  // A is less than B if and only if A[z] <= B[z] for all instances z and there
  // exists an index z' such that A[z'] < B[z']
  private def lessThan(a: Map[Node, Timestamp], b: Map[Node, Timestamp]): Boolean = {
    a.forall { case ((n, t)) => t <= b.getOrElse(n, Timestamp()) } &&
      (a.exists { case ((n, t)) => t < b.getOrElse(n, Timestamp()) } || (a.size < b.size))
  }
  
  override def toString: String = {
    versions.map { case ((x, y)) => x.name + " -> " + y }.mkString("VectorClock(", ", ", ")")
  }
}