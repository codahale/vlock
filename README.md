Vlock
=====

Because I wanted to do a vector clock implementation.


How To Build
------------

    sbt clean test package

(You'll need [simple-build-tool](http://code.google.com/p/simple-build-tool/).
It's pretty cool.)


How To Use
----------

Crack open a console using `sbt console`:

    import com.codahale.vlock.{Node, VectorClock}
    
    val server1 = Node("server 1")
    val server2 = Node("server 2")
    
    val clock = VectorClock()
    
    // update the clock from server1
    val change1 = clock + server1
    
    println(change1 > clock)
    println(change1 < clock)
    
    // update the clock from server2
    val change2 = clock + server2
    
    println(change2 > clock)
    println(change2 < clock)
    
    println(change1 <> change2)
    
    // merge the two clocks
    val reconciled = change1.merge(change2)
    println(change1 < reconciled)
    println(change2 < reconciled)