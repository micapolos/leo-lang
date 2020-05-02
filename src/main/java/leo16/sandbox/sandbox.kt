package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	load { bit }
	load { list }

	list
	append { zero.bit }
	append { one.bit }
	append { zero.bit }
	tail
}