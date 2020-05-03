package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

val ping = dictionary_ {
	ping.is_ { pong }
}

fun main() = run_ { ping }
