package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	zero.bit.negate
	gives { one.bit }

	one.bit.negate
	gives { zero.bit }

	zero.bit
	and { any.bit }
	gives { zero.bit }

	one.bit
	and { any.bit }
	gives { given.and.bit }
}