package leo16.library.dictionary

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

fun main() = run_ { number }

val number = dictionary_ {
	reflection.import

	import {
		dictionary {
			big.decimal.class_.is_ {
				"java.math.BigDecimal".text.name.class_
			}

			big.decimal.add.method.is_ {
				big.decimal.class_
				method {
					name { "add".text }
					parameter { list { big.decimal.class_ } }
				}
			}

			big.decimal.subtract.method.is_ {
				big.decimal.class_
				method {
					name { "subtract".text }
					parameter { list { big.decimal.class_ } }
				}
			}

			big.decimal.multiply.method.is_ {
				big.decimal.class_
				method {
					name { "multiply".text }
					parameter { list { big.decimal.class_ } }
				}
			}
		}
	}

	any.number
	plus { any.number }
	gives {
		given.number.native
		invoke {
			big.decimal.add.method
			parameter { list { given.plus.number.native } }
		}
		number
	}

	test { 2.number.plus { 3.number }.gives { 5.number } }

	any.number
	minus { any.number }
	gives {
		given.number.native
		invoke {
			big.decimal.subtract.method
			parameter { list { given.minus.number.native } }
		}
		number
	}

	test { 5.number.minus { 3.number }.gives { 2.number } }

	any.number
	times { any.number }
	gives {
		given.number.native
		invoke {
			big.decimal.multiply.method
			parameter { list { given.times.number.native } }
		}
		number
	}

	test { 2.number.times { 3.number }.gives { 6.number } }
	test {
		"12345678901234567890".number
		times { "12345678901234567890".number }
		gives { "152415787532388367501905199875019052100".number }
	}
}
