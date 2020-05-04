package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

fun main() = run_ { reflection }

val reflection = dictionary_ {
	native.reflection.import
	native.reflection.export

	import {
		dictionary {
			object_.class_.is_ { "java.lang.Object".text.name.class_ }
			boolean.object_.class_.is_ { "java.lang.Boolean".text.name.class_ }
			integer.class_.is_ { "java.lang.Integer".text.name.class_ }

			object_.string.method.is_ {
				object_.class_
				method {
					name { "toString".text }
					parameter { list }
				}
			}

			boolean.true_.field.is_ {
				boolean.object_.class_.field { name { "TRUE".text } }
			}

			boolean.false_.field.is_ {
				boolean.object_.class_.field { name { "FALSE".text } }
			}

			native.boolean.method.is_ {
				boolean.object_.class_
				method {
					name { "valueOf".text }
					parameter { list { boolean.class_ } }
				}
			}
		}
	}

	any.native.string.gives {
		given.string.native
		invoke {
			object_.string.method
			parameter { list }
		}
	}

	test {
		123.number.native.string.text.gives { "123".text }
	}

//	true_.native.is_ { boolean.true_.field.get }
//	false_.native.is_ { boolean.false_.field.get }
//
//	any.native.boolean.gives {
//		native.boolean.method.invoke { parameter { list { given.boolean.native } } }
//	}
//
//	test { true_.native.boolean.gives { true_.boolean } }
}
