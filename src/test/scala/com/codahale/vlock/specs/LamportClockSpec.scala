package com.codahale.vlock.specs

import com.codahale.vlock.{LamportClock, Timestamp}

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

import java.util.concurrent.{Executors, TimeUnit, CountDownLatch}

object ConcreteLamportClock extends LamportClock

class LamportClockSpec extends Spec with MustMatchers {
  describe("a Lamport clock being accessed across many threads") {
    it("returns strictly-increasing integers") {
      val startGate = new CountDownLatch(1)
      val pool = Executors.newFixedThreadPool(20)
      for (i <- 0 to 40) {
        pool.submit(new Runnable() {
          def run {
            var timestamps = List[Timestamp]()
            startGate.await
            for (j <- 0 to 1000) {
              timestamps = ConcreteLamportClock.timestamp :: timestamps
            }
            (timestamps.sort { _ > _ }) must equal (timestamps)
          }
        })
      }
      startGate.countDown
      pool.shutdown()
      pool.awaitTermination(30, TimeUnit.SECONDS)
    }
  }
}
