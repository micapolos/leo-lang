package lambda.v2

fun oneOf(n: Int, size: Int) = fn(size + 1) { arg(1 + n)(arg(1)) }
