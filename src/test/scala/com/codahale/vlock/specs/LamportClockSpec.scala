package com.codahale.vlock.specs

import com.codahale.vlock.{LamportClock, Timestamp}

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

import java.util.concurrent.{Executors, TimeUnit}

object ConcreteLamportClock extends LamportClock

class LamportClockSpec extends Spec with MustMatchers {
  describe("a Lamport clock being accessed across many threads") {
    it("returns strictly-increasing integers") {
      val pool = Executors.newFixedThreadPool(20)
      for (i <- 0 to 20) {
        pool.submit(new Runnable() {
          def run {
            var timestamps = List[Timestamp]()
            for (j <- 0 to 1000) {
              timestamps = ConcreteLamportClock.timestamp :: timestamps
            }
            (timestamps.sort { _ > _ }) must equal (timestamps)
          }
        })
      }
      
      pool.shutdown()
      pool.awaitTermination(30, TimeUnit.SECONDS)
    }
  }
}
