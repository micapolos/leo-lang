package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	text.load.import

	"Hello, ".text
	plus { "world!".text }
	cut {
		from { 7.number }
		to { 12.number }
	}
}