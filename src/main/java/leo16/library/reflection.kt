package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(reflection)
}

val reflection = dsl_ {
	use { native.reflection }
	export { native.reflection }

	use {
		object_.class_.is_ { "java.lang.Object".text.name.class_ }
		objects.class_.is_ { "java.util.Objects".text.name.class_ }
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

		object_.equals_.method.is_ {
			object_.class_
			method {
				name { "equals".text }
				parameter { list { item { object_.class_ } } }
			}
		}

		objects.equals_.method.is_ {
			objects.class_
			method {
				name { "equals".text }
				parameter {
					list {
						item { object_.class_ }
						item { object_.class_ }
					}
				}
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

	native.any.number.long
	does {
		long.number.native
		invoke {
			big.decimal.long.method
			parameter { empty.list }
		}.long
	}

	native.any.number.int
	does {
		int.number.native
		invoke {
			big.decimal.int.method
			parameter { empty.list }
		}.int
	}

	native.any.number.short
	does {
		short.number.native
		invoke {
			big.decimal.short.method
			parameter { empty.list }
		}.short
	}

	native.any.number.byte
	does {
		byte.number.native
		invoke {
			big.decimal.byte.method
			parameter { empty.list }
		}.byte
	}

	native.any.number.float
	does {
		float.number.native
		invoke {
			big.decimal.float.method
			parameter { empty.list }
		}.float
	}

	native.any.number.double
	does {
		double.number.native
		invoke {
			big.decimal.double.method
			parameter { empty.list }
		}.double
	}

	native.any.long.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.long.native } } } }
		number
	}

	native.any.int.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.int.native } } } }
		number
	}

	native.any.short.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.short.native } } } }
		number
	}

	native.any.byte.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.byte.native } } } }
		number
	}

	native.any.float.number
	does {
		double.big.decimal.method
		invoke { parameter { list { item { number.float.native } } } }
		number
	}

	native.any.double.number
	does {
		double.big.decimal.method
		invoke { parameter { list { item { number.double.native } } } }
		number
	}

	native.any.object_.string.does {
		string.object_.native
		invoke {
			object_.string.method
			parameter { empty.list }
		}
	}

	test {
		123.number.native.object_.string.text.equals_ { "123".text }
	}

	true_.boolean.native.is_ { boolean.true_.field.get }
	false_.boolean.native.is_ { boolean.false_.field.get }
}
