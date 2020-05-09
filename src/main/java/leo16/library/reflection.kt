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
					parameter { stack { item { long.class_ } } }
				}
			}

			double.big.decimal.method.is_ {
				big.decimal.class_
				method {
					name { "valueOf".text }
					parameter { stack { item { double.class_ } } }
				}
			}

			big.decimal.long.method.is_ {
				big.decimal.class_
				method {
					name { "longValueExact".text }
					parameter { empty.stack }
				}
			}

			big.decimal.int.method.is_ {
				big.decimal.class_
				method {
					name { "intValueExact".text }
					parameter { empty.stack }
				}
			}

			big.decimal.short.method.is_ {
				big.decimal.class_
				method {
					name { "shortValueExact".text }
					parameter { empty.stack }
				}
			}

			big.decimal.byte.method.is_ {
				big.decimal.class_
				method {
					name { "byteValueExact".text }
					parameter { empty.stack }
				}
			}

			big.decimal.float.method.is_ {
				big.decimal.class_
				method {
					name { "floatValue".text }
					parameter { empty.stack }
				}
			}

			big.decimal.double.method.is_ {
				big.decimal.class_
				method {
					name { "doubleValue".text }
					parameter { empty.stack }
				}
			}

			object_.string.method.is_ {
				object_.class_
				method {
					name { "toString".text }
					parameter { empty.stack }
				}
			}

			object_.equals_.method.is_ {
				object_.class_
				method {
					name { "equals".text }
					parameter { stack { item { object_.class_ } } }
				}
			}

			objects.equals_.method.is_ {
				objects.class_
				method {
					name { "equals".text }
					parameter {
						stack {
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
					parameter { stack { item { boolean.class_ } } }
				}
			}
		}
	}

	any.number.long
	gives {
		long.number.native
		invoke {
			big.decimal.long.method
			parameter { empty.stack }
		}.long
	}

	any.number.int
	gives {
		int.number.native
		invoke {
			big.decimal.int.method
			parameter { empty.stack }
		}.int
	}

	any.number.short
	gives {
		short.number.native
		invoke {
			big.decimal.short.method
			parameter { empty.stack }
		}.short
	}

	any.number.byte
	gives {
		byte.number.native
		invoke {
			big.decimal.byte.method
			parameter { empty.stack }
		}.byte
	}

	any.number.float
	gives {
		float.number.native
		invoke {
			big.decimal.float.method
			parameter { empty.stack }
		}.float
	}

	any.number.double
	gives {
		double.number.native
		invoke {
			big.decimal.double.method
			parameter { empty.stack }
		}.double
	}

	any.long.number
	gives {
		long.big.decimal.method
		invoke { parameter { stack { item { number.long.native } } } }
		number
	}

	any.int.number
	gives {
		long.big.decimal.method
		invoke { parameter { stack { item { number.int.native } } } }
		number
	}

	any.short.number
	gives {
		long.big.decimal.method
		invoke { parameter { stack { item { number.short.native } } } }
		number
	}

	any.byte.number
	gives {
		long.big.decimal.method
		invoke { parameter { stack { item { number.byte.native } } } }
		number
	}

	any.float.number
	gives {
		double.big.decimal.method
		invoke { parameter { stack { item { number.float.native } } } }
		number
	}

	any.double.number
	gives {
		double.big.decimal.method
		invoke { parameter { stack { item { number.double.native } } } }
		number
	}

	any.native.object_.string.gives {
		string.object_.native
		invoke {
			object_.string.method
			parameter { empty.stack }
		}
	}

	test {
		123.number.native.object_.string.text.gives { "123".text }
	}

	true_.boolean.native.is_ { boolean.true_.field.get }
	false_.boolean.native.is_ { boolean.false_.field.get }

	any.native.object_
	equals_ { any.native }
	gives {
		objects.equals_.method
		invoke {
			parameter {
				stack {
					item { object_.native }
					item { equals_.native }
				}
			}
		}
	}

	test {
		"hello".text.native.object_
		equals_ { "hello".text.native }.boolean
		gives { boolean { true_ } }
	}

	test {
		"hello".text.native.object_
		equals_ { "world".text.native }.boolean
		gives { boolean { false_ } }
	}
}
