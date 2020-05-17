package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	number
}

val number = compile_ {
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

			big.decimal.divide.method
			is_ {
				big.decimal.class_
				method {
					name { "divide".text }
					parameter {
						list {
							item { big.decimal.class_ }
							item { math.context.class_ }
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
							item { math.context.class_ }
						}
					}
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

			math.context.decimal.field
			is_ {
				math.context.class_
				field { name { "DECIMAL128".text } }
			}

			math.context.decimal
			is_ {
				math.context.decimal.field.get
			}
		}
	}

	any.native.number
	plus { any.native.number }
	does {
		number.native
		invoke {
			big.decimal.add.method
			parameter { list { item { plus.number.native } } }
		}
		number
	}

	test { 2.number plus { 3.number } equals_ { 5.number } }

	any.native.number
	minus { any.native.number }
	does {
		number.native
		invoke {
			big.decimal.subtract.method
			parameter { list { item { minus.number.native } } }
		}
		number
	}

	test { 5.number minus { 3.number } equals_ { 2.number } }

	any.native.number.negate
	does {
		0.number
		minus { negate.number }
	}

	test { 5.number.negate.equals_ { (-5).number } }

	any.native.number
	times { any.native.number }
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

	any.native.number
	divided { by { any.native.number } }
	does {
		number.native
		invoke {
			method { big.decimal.divide }
			parameter {
				list {
					item { divided.by.number.native }
					item { math.context.decimal }
				}
			}
		}
		number
	}

	test {
		15.number
		divided { by { 5.number } }
		equals_ { 3.number }
	}

	any.native.number
	modulo { any.native.number }
	does {
		number.native
		invoke {
			method { big.decimal.remainder }
			parameter {
				list {
					item { modulo.number.native }
					item { math.context.decimal }
				}
			}
		}
		number
	}

	test {
		15.number
		modulo { 7.number }
		equals_ { 1.number }
	}

	any.native.number.square.root
	does {
		root.square.number.native
		invoke {
			method { big.decimal.sqrt }
			parameter { list { item { math.context.decimal } } }
		}
		number
	}

	test { 25.number.square.root equals_ { 5.number } }
	test { 5.number.square.root equals_ { "2.236067977499789696409173668731276".number } }
}
