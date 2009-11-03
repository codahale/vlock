package com.codahale.vlock.specs

import com.codahale.vlock.Node

import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers

class NodeSpec extends Spec with MustMatchers {
  describe("a node") {
    val node = Node("one")
    
    it("is human-readable") {
      node.toString must equal ("Node(one)")
    }
    
    it("has a name") {
      node.name must equal ("one")
    }
    
    it("has an ID") {
      node.id must equal ("f97c5d29941bfb1b2fdab0874906ab82")
    }
  }
}
