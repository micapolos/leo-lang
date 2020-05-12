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
	}

	any.number.long
	does {
		long.number.native
		invoke {
			big.decimal.long.method
			parameter { empty.list }
		}.long
	}

	any.number.int
	does {
		int.number.native
		invoke {
			big.decimal.int.method
			parameter { empty.list }
		}.int
	}

	any.number.short
	does {
		short.number.native
		invoke {
			big.decimal.short.method
			parameter { empty.list }
		}.short
	}

	any.number.byte
	does {
		byte.number.native
		invoke {
			big.decimal.byte.method
			parameter { empty.list }
		}.byte
	}

	any.number.float
	does {
		float.number.native
		invoke {
			big.decimal.float.method
			parameter { empty.list }
		}.float
	}

	any.number.double
	does {
		double.number.native
		invoke {
			big.decimal.double.method
			parameter { empty.list }
		}.double
	}

	any.long.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.long.native } } } }
		number
	}

	any.int.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.int.native } } } }
		number
	}

	any.short.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.short.native } } } }
		number
	}

	any.byte.number
	does {
		long.big.decimal.method
		invoke { parameter { list { item { number.byte.native } } } }
		number
	}

	any.float.number
	does {
		double.big.decimal.method
		invoke { parameter { list { item { number.float.native } } } }
		number
	}

	any.double.number
	does {
		double.big.decimal.method
		invoke { parameter { list { item { number.double.native } } } }
		number
	}

	any.native.object_.string.does {
		string.object_.native
		invoke {
			object_.string.method
			parameter { empty.list }
		}
	}

	test {
		123.number.native.object_.string.text.does { "123".text }
	}

	true_.boolean.native.is_ { boolean.true_.field.get }
	false_.boolean.native.is_ { boolean.false_.field.get }

	any.native.object_
	equals_ { any.native }
	does {
		objects.equals_.method
		invoke {
			parameter {
				list {
					item { object_.native }
					item { equals_.native }
				}
			}
		}
	}

	test {
		"hello".text.native.object_
		equals_ { "hello".text.native }.boolean
		does { boolean { true_ } }
	}

	test {
		"hello".text.native.object_
		equals_ { "world".text.native }.boolean
		does { boolean { false_ } }
	}
}
