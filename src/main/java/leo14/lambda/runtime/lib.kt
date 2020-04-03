package leo14.lambda.runtime

val id = fn { it }

val first = fn { at0 -> fn { at0 } }
val second = fn { fn { at1 -> at1 } }

val pair = fn { first -> fn { second -> fn { which -> which(first)(second) } } }
val firstOfTwo = fn { firstOfTwo -> fn { forFirst -> fn { forFirst(firstOfTwo) } } }
val secondOfTwo = fn { secondOfTwo -> fn { fn { forSecond -> forSecond(secondOfTwo) } } }

val X.asString get() = (this as String)
val X.asInt get() = (this as Int)

fun intOp(fn: Int.() -> Int) = fn { int -> int.asInt.fn() }
fun intOp2(fn: Int.(Int) -> Int) = fn { i1 -> fn { i2 -> i1.asInt.fn(i2.asInt) } }
val intNegate = intOp(Int::unaryMinus)
val intPlusInt = intOp2(Int::plus)
val intMinusInt = intOp2(Int::minus)
val intTimesInt = intOp2(Int::times)
val intString = fn { int -> int.toString() }

val stringLength = fn { string -> string.asString.length }
val stringPlusString = fn { s1 -> fn { s2 -> s1.asString + s2.asString } }