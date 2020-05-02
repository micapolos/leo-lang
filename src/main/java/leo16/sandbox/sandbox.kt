package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	import { base.load }
	import { bit.load }
	import { list.load }

	list
	append { zero.bit }
	append { one.bit }
	append { zero.bit }
	append { keyword }
}