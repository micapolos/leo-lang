package leo15.sandbox

import leo15.dsl.*

fun main() {
	main_ {
		number(10)
		number(12)
		number(13)
		as_ { repeating { number } }
		type
	}
}
