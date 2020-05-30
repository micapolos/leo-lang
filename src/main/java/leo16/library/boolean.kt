package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(boolean)
}

val boolean = dsl_ {
	boolean.any.is_ { boolean { true_.or { false_ } } }

	false_.boolean.negate is_ { true_.boolean }
	true_.boolean.negate.is_ { false_.boolean }

	false_.boolean and { false_.boolean } is_ { false_.boolean }
	false_.boolean and { true_.boolean } is_ { false_.boolean }
	true_.boolean and { false_.boolean } is_ { false_.boolean }
	true_.boolean and { true_.boolean } is_ { true_.boolean }

	false_.boolean or { false_.boolean } is_ { false_.boolean }
	false_.boolean or { true_.boolean } is_ { true_.boolean }
	true_.boolean or { false_.boolean } is_ { true_.boolean }
	true_.boolean or { true_.boolean } is_ { true_.boolean }

	false_.boolean xor { false_.boolean } is_ { true_.boolean }
	false_.boolean xor { true_.boolean } is_ { false_.boolean }
	true_.boolean xor { false_.boolean } is_ { false_.boolean }
	true_.boolean xor { true_.boolean } is_ { true_.boolean }
}