package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	reflection
}

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
					parameter { list { item { long.class_ } } }
				}
			}

			double.big.decimal.method.is_ {
				big.decimal.class_
				method {
					name { "valueOf".text }
					parameter { list { item { double.class_ } } }
				}
			}

			big.decimal.long.method.is_ {
				big.decimal.class_
				method {
					name { "longValueExact".text }
					parameter { empty.list }
				}
			}

			big.decimal.int.method.is_ {
				big.decimal.class_
				method {
					name { "intValueExact".text }
					parameter { empty.list }
				}
			}

			big.decimal.short.method.is_ {
				big.decimal.class_
				method {
					name { "shortValueExact".text }
					parameter { empty.list }
				}
			}

			big.decimal.byte.method.is_ {
				big.decimal.class_
				method {
					name { "byteValueExact".text }
					parameter { empty.list }
				}
			}

			big.decimal.float.method.is_ {
				big.decimal.class_
				method {
					name { "floatValue".text }
					parameter { empty.list }
				}
			}

			big.decimal.double.method.is_ {
				big.decimal.class_
				method {
					name { "doubleValue".text }
					parameter { empty.list }
				}
			}

			object_.string.method.is_ {
				object_.class_
				method {
					name { "toString".text }
					parameter { empty.list }
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
					parameter { list { item { boolean.class_ } } }
				}
			}
		}
	}

	any.number.long
	gives {
		long.number.native
		invoke {
			big.decimal.long.method
			parameter { empty.list }
		}.long
	}

	any.number.int
	gives {
		int.number.native
		invoke {
			big.decimal.int.method
			parameter { empty.list }
		}.int
	}

	any.number.short
	gives {
		short.number.native
		invoke {
			big.decimal.short.method
			parameter { empty.list }
		}.short
	}

	any.number.byte
	gives {
		byte.number.native
		invoke {
			big.decimal.byte.method
			parameter { empty.list }
		}.byte
	}

	any.number.float
	gives {
		float.number.native
		invoke {
			big.decimal.float.method
			parameter { empty.list }
		}.float
	}

	any.number.double
	gives {
		double.number.native
		invoke {
			big.decimal.double.method
			parameter { empty.list }
		}.double
	}

	any.long.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { item { number.long.native } } } }
		number
	}

	any.int.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { item { number.int.native } } } }
		number
	}

	any.short.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { item { number.short.native } } } }
		number
	}

	any.byte.number
	gives {
		long.big.decimal.method
		invoke { parameter { list { item { number.byte.native } } } }
		number
	}

	any.float.number
	gives {
		double.big.decimal.method
		invoke { parameter { list { item { number.float.native } } } }
		number
	}

	any.double.number
	gives {
		double.big.decimal.method
		invoke { parameter { list { item { number.double.native } } } }
		number
	}

	any.native.object_.string.gives {
		string.object_.native
		invoke {
			object_.string.method
			parameter { empty.list }
		}
	}

	test {
		123.number.native.object_.string.text.gives { "123".text }
	}

	true_.boolean.native.is_ { boolean.true_.field.get }
	false_.boolean.native.is_ { boolean.false_.field.get }

	any.native.boolean.gives {
		native.boolean.method
		invoke { parameter { list { boolean.native } } }
		boolean
	}
}
