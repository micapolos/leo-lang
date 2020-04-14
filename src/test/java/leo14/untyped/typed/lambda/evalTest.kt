package leo14.untyped.typed.lambda

import leo14.bigDecimal
import leo14.untyped.dsl2.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun literals() {
		script_ { number(10) }.evals { number(10) }
		script_ { text("foo") }.evals { text("foo") }
	}

	@Test
	fun literalJava() {
		script_ { text("foo").java }.evals { java_("foo") }
		script_ { number(123).java }.evals { java_(123.bigDecimal) }
	}

	@Test
	fun this_() {
		script_ { number(123).this_ }.evals { number(123) }
	}

	@Test
	fun structs() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.evals {
			point {
				x { number(10) }
				y { number(20) }
			}
		}
	}

	@Test
	fun get() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.x
		}.evals {
			x { number(10) }
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.y
		}.evals {
			y { number(20) }
		}
	}

	@Test
	fun set() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
			set { x { number(30) } }
		}.evals {
			point {
				x { number(30) }
				y { number(20) }
			}
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
			set { y { number(30) } }
		}.evals {
			point {
				x { number(10) }
				y { number(30) }
			}
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
			set { z { number(30) } }
		}.evals {
			point {
				x { number(10) }
				y { number(20) }
			}
			set { z { number(30) } }
		}
	}

	@Test
	fun normalization() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.center
		}.evals {
			center {
				point {
					x { number(10) }
					y { number(20) }
				}
			}
		}
	}

	@Test
	fun type() {
		script_ { number(10).type }.evals { number }
		script_ { text("foo").type }.evals { text }

		script_ { number(10).java.type }.evals { java }
		script_ { text("foo").java.type }.evals { java }

		script_ {
			point {
				x { number(10) }
				y { number(10) }
			}.type
		}.evals {
			point {
				x { number }
				y { number }
			}
		}
	}

	@Test
	fun giveGiven() {
		script_ {
			number(10).give { given }
		}.evals {
			given { number(10) }
		}
	}

	@Test
	fun giveGivenNumber() {
		script_ {
			number(10).give { given.number }
		}.evals {
			number(10)
		}
	}

	@Test
	fun is_() {
		script_ {
			x.is_ { number(123) }
		}.evals {
			nothing_
		}
	}

	@Test
	fun manyIs() {
		script_ {
			x.is_ { number(123) }
			y.is_ { number(124) }
			z.is_ { number(125) }
		}.evals {
			nothing_
		}
	}

	@Test
	fun manyIsAndGet() {
		script_ {
			x.is_ { number(123) }
			y.is_ { number(124) }
			z.is_ { number(125) }
			y
		}.evals {
			number(124)
		}
	}

	@Test
	fun is_staticType() {
		script_ {
			number.is_ { result }
			number
		}.evals {
			result
		}

		script_ {
			number.is_ { result }
			number(123)
		}.evals {
			number(123)
		}
	}

	@Test
	fun is_dynamicType() {
		script_ {
			number(123).is_ { result }
		}.evals {
			number(123).is_ { result }
		}
	}

	@Test
	fun gives() {
		script_ {
			number.gives { given }
		}.evals {
			nothing_
		}
	}

	@Test
	fun givesGiven() {
		script_ {
			number.gives { given }
			number(123)
		}.evals {
			given { number(123) }
		}
	}

	@Test
	fun givesGivenNumber() {
		script_ {
			number.gives { given.number }
			number(123)
		}.evals {
			number(123)
		}
	}

	@Test
	fun givesGivenNumberJava() {
		script_ {
			number.gives { given.number.java }
			number(123)
		}.evals {
			java_(123.bigDecimal)
		}
	}

	@Test
	fun failingUseCase1() {
		script_ {
			my.number.is_ { number(123) }
			the.number.is_ { my.number }
			the.number
		}.evals {
			number(123)
		}
	}
}