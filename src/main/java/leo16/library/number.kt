package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	number
}

val number = dictionary_ {
	reflection.import

	import {
		dictionary {
			big.decimal.class_
			is_ {
				"java.math.BigDecimal".text.name.class_
			}

			math.context.class_
			is_ {
				"java.math.MathContext".text.name.class_
			}

			big.decimal.add.method
			is_ {
				big.decimal.class_
				method {
					name { "add".text }
					parameter { list { item { big.decimal.class_ } } }
				}
			}

			big.decimal.subtract.method
			is_ {
				big.decimal.class_
				method {
					name { "subtract".text }
					parameter { list { item { big.decimal.class_ } } }
				}
			}

			big.decimal.multiply.method
			is_ {
				big.decimal.class_
				method {
					name { "multiply".text }
					parameter { list { item { big.decimal.class_ } } }
				}
			}

			big.decimal.multiply.method
			is_ {
				big.decimal.class_
				method {
					name { "multiply".text }
					parameter { list { item { big.decimal.class_ } } }
				}
			}

			big.decimal.sqrt.method
			is_ {
				big.decimal.class_
				method {
					name { "sqrt".text }
					parameter { list { item { math.context.class_ } } }
				}
			}

			math.context.unlimited.field
			is_ {
				math.context.class_
				field { name { "UNLIMITED".text } }
			}

			math.context.unlimited
			is_ {
				math.context.unlimited.field.get
			}
		}
	}

	any.number
	plus { any.number }
	gives {
		number.native
		invoke {
			big.decimal.add.method
			parameter { list { item { plus.number.native } } }
		}
		number
	}

	test { 2.number plus { 3.number } gives { 5.number } }

	any.number
	minus { any.number }
	gives {
		number.native
		invoke {
			big.decimal.subtract.method
			parameter { list { item { minus.number.native } } }
		}
		number
	}

	test { 5.number minus { 3.number } gives { 2.number } }

	any.number
	times { any.number }
	gives {
		number.native
		invoke {
			big.decimal.multiply.method
			parameter { list { item { times.number.native } } }
		}
		number
	}

	test { 2.number times { 3.number } gives { 6.number } }
	test {
		"12345678901234567890".number
		times { "12345678901234567890".number }
		gives { "152415787532388367501905199875019052100".number }
	}

	any.number.squared gives { squared.number.times { squared.number } }
	test { 5.number.squared gives { 25.number } }

	any.number.square.root.gives {
		root.square.number.native
		invoke {
			method { big.decimal.sqrt }
			parameter { list { item { math.context.unlimited } } }
		}
		number
	}

	test { 25.number.square.root gives { 5.number } }
}
