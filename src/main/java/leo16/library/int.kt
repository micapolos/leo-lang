package leo16.library

import leo15.dsl.*
import leo16.value_

val int = value_ {
	dictionary {
		any.int.number
		gives {
			"java.math.BigDecimal".text.name.class_
			method {
				name { "valueOf".text }
				parameter { list { long.class_ } }
			}
			invoke { parameter { list { given.number.int.native } } }
			number
		}
	}
}