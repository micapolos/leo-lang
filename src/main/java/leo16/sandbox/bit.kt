package leo16.sandbox

import leo15.dsl.*
import leo16.run_

val bitLibrary = dsl_ {
	library {
		zero.bit.negate.is_ { one.bit }
		one.bit.negate.is_ { zero.bit }

		zero.bit.and { zero.bit }.is_ { zero.bit }
		zero.bit.and { one.bit }.is_ { zero.bit }
		one.bit.and { zero.bit }.is_ { zero.bit }
		one.bit.and { one.bit }.is_ { one.bit }

		zero.bit.or { zero.bit }.is_ { zero.bit }
		zero.bit.or { one.bit }.is_ { one.bit }
		one.bit.or { zero.bit }.is_ { one.bit }
		one.bit.or { one.bit }.is_ { one.bit }

		zero.bit.xor { zero.bit }.is_ { one.bit }
		zero.bit.xor { one.bit }.is_ { zero.bit }
		one.bit.xor { zero.bit }.is_ { zero.bit }
		one.bit.xor { one.bit }.is_ { one.bit }
	}
}

fun main() = run_ { bitLibrary() }