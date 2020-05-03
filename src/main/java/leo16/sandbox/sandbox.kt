package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	number.load.import

	2.number
	plus { 3.number }
}