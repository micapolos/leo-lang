package leo16.library

import leo15.dsl.*
import leo16.value_

val number = value_ {
	dictionary {
		import { reflection.load }

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
			}
		}

		any.int.number
		gives {
			long.big.decimal.method
			invoke { parameter { list { given.number.int.native } } }
			number
		}
	}
}