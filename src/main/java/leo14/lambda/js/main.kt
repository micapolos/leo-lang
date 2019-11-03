package leo14.lambda.js

import leo14.dsl.*
import leo14.lambda.first
import leo14.lambda.pair
import leo14.lambda.second

val helloWorld get() = term("Hello, world!")
val pair = pair(term("first"), term("second"))
val pairFirst get() = pair.first
val pairSecond get() = pair.second

val helloWorldScript = native("Hello, world!")
val pairFirstScript =
	function(function(argument(previous)))
		.apply(native("first"))
		.apply(native("second"))


fun main() = helloWorldScript.show
