package com.codahale.vlock

import java.util.concurrent.atomic.AtomicLong

/**
 * A strictly-increasing clock. Guaranteed to return a value greater than its
 * last return value.
 */
trait LamportClock extends Clock {
  private val counter = new AtomicLong(System.currentTimeMillis)
  
  def timestamp: Timestamp = {
    var newTime: Long = 0
    while(newTime == 0) {
      val last = counter.get
      val current = System.currentTimeMillis
      val next = if (current > last) current else last + 1
      if (counter.compareAndSet(last, next)) {
        newTime = next
      }
    }
    return Timestamp(newTime)
  }
}