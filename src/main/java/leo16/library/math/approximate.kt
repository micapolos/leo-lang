package leo16.library.math

import leo.base.print
import leo15.dsl.*
import leo16.compile_
import leo16.nativeString
import java.math.BigDecimal

fun main() {
	approximate.value.print
}

val approximate = compile_ {
	use { reflection }
	use { number }
	use { big.decimal.native }
	use { double.native }
	use { math.native }

	approximate.any.is_ { native.approximate }

	number.any.approximate
	does {
		approximate.number.native
		invoke {
			method { big.decimal.double.value }
			parameter { empty.list }
		}
		approximate
	}

	test {
		123.number.approximate.as_ { text }
		equals_ { "approximate ${123.0.nativeString}".text }
	}

	test {
		123.number.approximate.native.object_.class_.content.as_ { text }
		equals_ { java.lang.Double::class.java.nativeString.text }
	}

	approximate.any.number
	does {
		big.decimal.double.constructor
		invoke {
			parameter {
				list {
					item { number.approximate.native }
				}
			}
		}
		number
		// TODO: Handle arithmetic errors, infinity, NaN and so on...
	}

	test {
		0.1.number.approximate.number.equals_ { BigDecimal(0.1).number }
	}

	approximate.any
	plus { approximate.any }
	does {
		method { double.plus }
		invoke {
			parameter {
				list {
					item { approximate.native }
					item { plus.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		2.number.approximate
		plus { 3.number.approximate }
		equals_ { 5.number.approximate }
	}

	approximate.any
	minus { approximate.any }
	does {
		method { double.minus }
		invoke {
			parameter {
				list {
					item { approximate.native }
					item { minus.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		5.number.approximate
		minus { 3.number.approximate }
		equals_ { 2.number.approximate }
	}

	approximate.any
	times { approximate.any }
	does {
		method { double.times }
		invoke {
			parameter {
				list {
					item { approximate.native }
					item { times.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		2.number.approximate
		times { 3.number.approximate }
		equals_ { 6.number.approximate }
	}

	approximate.any.minus
	does {
		0.number.approximate
		minus { minus.approximate }
	}

	test {
		2.number.approximate.minus
		equals_ { (-2).number.approximate }
	}

	approximate.any
	by { approximate.any }
	does {
		method { double.div }
		invoke {
			parameter {
				list {
					item { approximate.native }
					item { by.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		6.number.approximate
		by { 3.number.approximate }
		equals_ { 2.number.approximate }
	}

	approximate.any
	modulo { approximate.any }
	does {
		method { double.mod }
		invoke {
			parameter {
				list {
					item { approximate.native }
					item { modulo.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		7.number.approximate
		modulo { 4.number.approximate }
		equals_ { 3.number.approximate }
	}

	approximate.any.sinus
	does {
		method { math.double.sin }
		invoke {
			parameter {
				list {
					item { sinus.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		0.number.approximate.sinus
		equals_ { 0.number.approximate }
	}

	approximate.any.cosinus
	does {
		method { math.double.cos }
		invoke {
			parameter {
				list {
					item { cosinus.approximate.native }
				}
			}
		}
		approximate
	}

	test {
		0.number.approximate.cosinus
		equals_ { 1.number.approximate }
	}

	number.any.read
	does { read.number.approximate }

	approximate.any.reflect
	does { reflect.approximate.number }
}
