package leo16.lib.dictionary

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
			big.decimal.class_.is_ { "java.math.BigDecimal".text.name.class_ }

			long.big.decimal.method.is_ {
				big.decimal.class_
				method {
					name { "valueOf".text }
					parameter { list { long.class_ } }
				}
			}

			double.big.decimal.method.is_ {
				big.decimal.class_
				method {
					name { "valueOf".text }
					parameter { list { double.class_ } }
				}
			}

			big.decimal.long.method.is_ {
				big.decimal.class_
				method {
					name { "longValueExact".text }
					parameter { list }
				}
			}

			big.decimal.int.method.is_ {
				big.decimal.class_
				method {
					name { "intValueExact".text }
					parameter { list }
				}
			}

			big.decimal.short.method.is_ {
				big.decimal.class_
				method {
					name { "shortValueExact".text }
					parameter { list }
				}
			}

			big.decimal.byte.method.is_ {
				big.decimal.class_
				method {
					name { "byteValueExact".text }
					parameter { list }
				}
			}

			big.decimal.float.method.is_ {
				big.decimal.class_
				method {
					name { "floatValue".text }
					parameter { list }
				}
			}

			big.decimal.double.method.is_ {
				big.decimal.class_
				method {
					name { "doubleValue".text }
					parameter { list }
				}
			}

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

	any.number.long
	gives {
		given.long.number.native
		invoke {
			big.decimal.long.method
			parameter { list }
		}.long
	}

	any.number.int
	gives {
		given.int.number.native
		invoke {
			big.decimal.int.method
			parameter { list }
		}.int
	}

	any.number.short
	gives {
		given.short.number.native
		invoke {
			big.decimal.short.method
			parameter { list }
		}.short
	}

	any.number.byte
	gives {
		given.byte.number.native
		invoke {
			big.decimal.byte.method
			parameter { list }
		}.byte
	}

	any.number.float
	gives {
		given.float.number.native
		invoke {
			big.decimal.float.method
			parameter { list }
		}.float
	}

	any.number.double
	gives {
		given.double.number.native
		invoke {
			big.decimal.double.method
			parameter { list }
		}.double
	}

	any.long.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { given.number.long.native } } }
		number
	}

	any.int.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { given.number.int.native } } }
		number
	}

	any.short.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { given.number.short.native } } }
		number
	}

	any.byte.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { given.number.byte.native } } }
		number
	}

	any.float.number
	gives {
		double.big.decimal.method
		invoke { parameter { list { given.number.float.native } } }
		number
	}

	any.double.number
	gives {
		double.big.decimal.method
		invoke { parameter { list { given.number.double.native } } }
		number
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

	true_.boolean.native.is_ { boolean.true_.field.get }
	false_.boolean.native.is_ { boolean.false_.field.get }

	any.native.boolean.gives {
		native.boolean.method
		invoke { parameter { list { given.boolean.native } } }
		boolean
	}
}
