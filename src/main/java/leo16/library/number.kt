package leo16.library

import leo15.dsl.*
import leo16.value_

val number = value_ {
	dictionary {
		reflection.dictionary.import

		import {
			dictionary {
				big.decimal.class_.is_ {
					"java.math.BigDecimal".text.name.class_
				}

				long.big.decimal.method.is_ {
					big.decimal.class_
					method {
						name { "valueOf".text }
						parameter { list { long.class_ } }
					}
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

		any.int.number
		gives {
			long.big.decimal.method
			invoke { parameter { list { given.number.int.native } } }
			number
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
	}
}