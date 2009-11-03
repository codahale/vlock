package com.codahale.vlock

import java.security.MessageDigest

object Node {
  def apply(name: String): Node = {
    Node(hash(name), name)
  }
  
  private def hash(name: String): String = {
    val md5 = MessageDigest.getInstance("MD5")
    md5.update(name.getBytes)
    md5.digest().map { b => "%02x".format(0xFF & b) }.mkString
  }
}

case class Node private(id: String, name: String) {
  override def toString = name.mkString("Node(", "", ")")
}
