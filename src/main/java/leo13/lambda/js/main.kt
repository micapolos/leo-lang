package leo13.lambda.js

import leo13.js2.dsl.js
import leo13.lambda.first
import leo13.lambda.pair
import leo13.lambda.second
import leo13.lambda.value

val helloWorld get() = value(js("Hello, world!"))
val pairFirst get() = pair(value(js("first")), value(js("second"))).first
val pairSecond get() = pair(value(js("first")), value(js("second"))).second

fun main() = pairFirst.show
