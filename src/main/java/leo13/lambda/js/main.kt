package leo13.lambda.js

import leo13.lambda.first
import leo13.lambda.pair
import leo13.lambda.second

val helloWorld get() = value("Hello, world!")
val pair = pair(value("first"), value("second"))
val pairFirst get() = pair.first
val pairSecond get() = pair.second

fun main() = helloWorld.show
