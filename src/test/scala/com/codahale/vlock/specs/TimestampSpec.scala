package com.codahale.vlock.specs

import com.codahale.vlock.Timestamp

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

class TimestampSpec extends Spec with MustMatchers {
  describe("a timestamp") {
    it("is human-readable") {
      Timestamp(0xDECAFBAD).toString must equal ("ffffffffdecafbad")
    }
    
    it("is naturally ordered") {
      val timestamps = List(Timestamp(3), Timestamp(2), Timestamp(1))
      
      timestamps.sort { _ < _ } must equal (timestamps.reverse)
    }
    
    it("can select the larger of two timestamps") {
      val one = Timestamp(1)
      val two = Timestamp(2)
      
      (one.max(two)) must equal (two)
      (two.max(one)) must equal (two)
    }
  }
}
