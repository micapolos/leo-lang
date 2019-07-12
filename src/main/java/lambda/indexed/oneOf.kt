package lambda.indexed

fun oneOf(n: Int, size: Int) = fn(size + 1) { arg(1 + n)(arg(1)) }
