package leo13.lambda.js

import leo13.js2.dsl.js
import leo13.lambda.expr
import leo13.lambda.first
import leo13.lambda.pair
import leo13.lambda.second

val helloWorld get() = expr(js("Hello, world!"))
val pairFirst get() = pair(expr(js("first")), expr(js("second"))).first
val pairSecond get() = pair(expr(js("first")), expr(js("second"))).second

fun main() = pairFirst.show
