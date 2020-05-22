package leo16.library.native.decimal

import leo15.dsl.*
import leo16.compile_

val big = compile_ {
	use { reflection }
	use { math.context.native }
	use { string.native }

	big.decimal.class_
	is_ { "java.math.BigDecimal".text.name.class_ }

	big.decimal.string.constructor
	is_ {
		big.decimal.class_
		constructor { parameter { list { item { string.class_ } } } }
	}

	big.decimal.double.constructor
	is_ {
		big.decimal.class_
		constructor { parameter { list { item { double.class_ } } } }
	}

	big.decimal.double.context.constructor
	is_ {
		big.decimal.class_
		constructor {
			parameter {
				list {
					item { double.class_ }
					item { math.context.class_ }
				}
			}
		}
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

	big.decimal.divide.method
	is_ {
		big.decimal.class_
		method {
			name { "divide".text }
			parameter {
				list {
					item { big.decimal.class_ }
				}
			}
		}
	}

	big.decimal.remainder.method
	is_ {
		big.decimal.class_
		method {
			name { "remainder".text }
			parameter {
				list {
					item { big.decimal.class_ }
				}
			}
		}
	}

	big.decimal.sqrt.context.method
	is_ {
		big.decimal.class_
		method {
			name { "sqrt".text }
			parameter { list { item { math.context.class_ } } }
		}
	}

	big.decimal.double.value.method
	is_ {
		big.decimal.class_
		method {
			name { "doubleValue".text }
			parameter { empty.list }
		}
	}
}