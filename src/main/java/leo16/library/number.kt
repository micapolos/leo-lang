package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	number
}

val number = compile_ {
	use { reflection }
	use { big.decimal.native }
	use { math.context.native }

	number.any.is_ { native.number }

	number.any
	plus { native.number }
	does {
		number.native
		invoke {
			big.decimal.add.method
			parameter { list { item { plus.number.native } } }
		}
		number
	}

	test { 2.number plus { 3.number } equals_ { 5.number } }

	number.any
	minus { number.any }
	does {
		number.native
		invoke {
			big.decimal.subtract.method
			parameter { list { item { minus.number.native } } }
		}
		number
	}

	test { 5.number minus { 3.number } equals_ { 2.number } }

	number.any.minus
	does {
		0.number
		minus { minus.number }
	}

	test { 5.number.minus.equals_ { (-5).number } }

	number.any
	times { number.any }
	does {
		number.native
		invoke {
			big.decimal.multiply.method
			parameter { list { item { times.number.native } } }
		}
		number
	}

	test { 2.number times { 3.number } equals_ { 6.number } }
	test {
		"12345678901234567890".number
		times { "12345678901234567890".number }
		equals_ { "152415787532388367501905199875019052100".number }
	}

	number.any
	by { number.any }
	does {
		input.is_ { content }
		number.native
		invoke {
			method { big.decimal.divide }
			parameter {
				list {
					item { by.number.native }
				}
			}
		}
		do_ {
			native.object_.class_
			equals_ { big.decimal.class_ }
			match {
				true_ { native.number }
				false_ { input }
			}
		}
	}

	test {
		15.number.by { 5.number }
		equals_ { 3.number }
	}

	test {
		15.number.by { 11.number }
		equals_ { quote { 15.number.by { 11.number } } }
	}

	number.any
	modulo { number.any }
	does {
		input.is_ { content }
		number.native
		invoke {
			method { big.decimal.remainder }
			parameter {
				list {
					item { modulo.number.native }
				}
			}
		}
		do_ {
			native.object_.class_
			equals_ { big.decimal.class_ }
			match {
				true_ { native.number }
				false_ { input }
			}
		}
	}

	test {
		15.number
		modulo { 7.number }
		equals_ { 1.number }
	}

	number.any.square.root
	does {
		input.is_ { content }
		root.square.number.native
		invoke {
			method { big.decimal.sqrt.context }
			parameter { list { item { math.context.unlimited } } }
		}
		do_ {
			native.object_.class_
			equals_ { big.decimal.class_ }
			match {
				true_ { native.number }
				false_ { input }
			}
		}
	}

	test { 25.number.square.root equals_ { 5.number } }
	test { 5.number.square.root equals_ { quote { root { square { 5.number } } } } }

	number.any.text
	does { text.number.as_ { meta { text } } }
	test { 123.number.text.equals_ { "123".text } }

	// TODO: Move to text?
	native.text.number
	does {
		big.decimal.string.constructor
		invoke { parameter { list { item { number.text.native } } } }
		do_ {
			native.object_.class_
			equals_ { big.decimal.class_ }
			match {
				true_ { native.number }
				false_ { number }
			}
		}
	}

	test { "123".text.number.equals_ { 123.number } }
	test { "gaga".text.number.equals_ { quote { number { "gaga".text } } } }
}
