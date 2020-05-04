package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

fun main() = run_ { reflection }

val reflection = dictionary_ {
	native.reflection.export

	import {
		dictionary {
			boolean.class_.is_ { "java.lang.Boolean".text.name.class_ }
			integer.class_.is_ { "java.lang.Integer".text.class_ }

			boolean.true_.field.is_ {
				boolean.class_.field { name { "TRUE".text } }
			}

			boolean.false_.field.is_ {
				boolean.class_.field { name { "FALSE".text } }
			}
		}
	}

	true_.native.is_ { boolean.true_.field.get }
	false_.native.is_ { boolean.false_.field.get }

	test { true_.native.matches { any.native } }
	test { false_.native.matches { any.native } }
}
