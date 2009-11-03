package com.codahale.vlock

/**
 * An abstract trait defining a class or object capable of returning a
 * timestamp.
 */
trait Clock {
  def timestamp: Timestamp
}
