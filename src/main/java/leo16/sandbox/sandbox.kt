package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	load { bit }

	list
	append { zero.bit }
	append { one.bit }
	append { zero.bit }
	match {
		empty.is_ { ok }
		any.link.is_ { ok.not }
	}
}