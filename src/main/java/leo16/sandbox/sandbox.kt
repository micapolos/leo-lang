package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	int.load.import

	2.int
	plus { 3.int }
}