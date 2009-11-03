package com.codahale.vlock.specs

import com.codahale.vlock.{VectorClock, Node, Timestamp}

import org.scalatest.Spec
import org.scalatest.matchers.{BeMatcher, MustMatchers}

object FixedClock extends Clock {
  def timestamp = Timestamp(0xDEADBEEF) 
}

class VectorClockSpec extends Spec with MustMatchers {
  describe("a vector clock") {
    val node = Node("one")
    val clock = VectorClock(FixedClock, Map()) + node
    
    it("is human-readable") {
      clock.toString must equal ("VectorClock(one -> ffffffffdeadbeef)")
    }
  }
  
  describe("a blank clock") {
    val one = Node("one")
    val clock = VectorClock()
    
    it("is not less than another blank clock") {
      val anotherClock = VectorClock()
      (clock < anotherClock) must be (false)
    }
    
    it("is is less than another clock with a single increment") {
      val anotherClock = VectorClock() + one
      (clock < anotherClock) must be (true)
    }
    
    it("is equivalent to another blank clock") {
      val anotherClock = VectorClock()
      (clock == anotherClock) must be (true)
    }
  }
  
  describe("a clock incremented once by a single node") {
    val one = Node("one")
    val two = Node("two")
    val clock = VectorClock() + one
    
    it("is less than another clock incremented twice by the same node") {
      (clock < VectorClock() + one + one) must be (true)
    }
    
    it("is less than another clock incremented once by the same node and then once by another node") {
      (clock < VectorClock() + one + two) must be (true)
    }
    
    it("is concurrent with another clock incremented once by another node") {
      (clock <> (VectorClock() + two)) must be (true)
    }
  }
  
  describe("the Riak examples") {
    val A = Node("one")
    val B = Node("two")
    val C = Node("three")
    
    val a = VectorClock()
    val b = VectorClock()
    
    val a1 = a + A
    val b1 = b + B
    
    it("passes blank clock incrementing") {
      (a1 > a) must equal (true)
      (b1 > b) must equal (true)
      
      (a1 > b) must equal (true)
      (b1 > a) must equal (true)
      
      (b1 > a1) must equal (false)
      (a1 > b1) must equal (false)
    }
    
    it("passes merging behavior") {
      var a2 = a1 + A
      var c = a2.merge(b1)
      var c1 = c + C
      
      (c1 > a2) must equal (true)
      (c1 > b1) must equal (true)
    }
  }
}
