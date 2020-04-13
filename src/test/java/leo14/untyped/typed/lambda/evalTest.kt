package leo14.untyped.typed.lambda

import leo14.bigDecimal
import leo14.untyped.dsl2.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun literals() {
		script_ { number(10) }.evals_ { number(10) }
		script_ { text("foo") }.evals_ { text("foo") }
	}

	@Test
	fun literalJava() {
		script_ { text("foo").java }.evals_ { java_("foo") }
		script_ { number(123).java }.evals_ { java_(123.bigDecimal) }
	}

	@Test
	fun structs() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.evals_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}
	}

	@Test
	fun fieldAccess() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.x
		}.evals_ {
			x { number(10) }
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.y
		}.evals_ {
			y { number(20) }
		}
	}

	@Test
	fun normalization() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.center
		}.evals_ {
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
		script_ { number(10).type }.evals_ { number }
		script_ { text("foo").type }.evals_ { text }

		script_ { number(10).java.type }.evals_ { java }
		script_ { text("foo").java.type }.evals_ { java }

		script_ {
			point {
				x { number(10) }
				y { number(10) }
			}.type
		}.evals_ {
			point {
				x { number }
				y { number }
			}
		}

	}

	@Test
	fun is_() {
		script_ {
			x.is_ { number(123) }
		}.evals_ {
			nothing_
		}
	}

	@Test
	fun manyIs() {
		script_ {
			x.is_ { number(123) }
			y.is_ { number(124) }
			z.is_ { number(125) }
		}.evals_ {
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
		}.evals_ {
			number(124)
		}
	}

	@Test
	fun is_staticType() {
		script_ {
			number.is_ { result }
			number
		}.evals_ {
			result
		}

		script_ {
			number.is_ { result }
			number(123)
		}.evals_ {
			number(123)
		}
	}

	@Test
	fun is_dynamicType() {
		script_ {
			number(123).is_ { result }
		}.evals_ {
			number(123).is_ { result }
		}
	}

	@Test
	fun does() {
		script_ {
			number.does { given }
		}.evals_ {
			nothing_
		}
	}

	@Test
	fun doesStaticAccess() {
		script_ {
			number.does { text("foo") }
			number(123)
		}.evals_ {
			text("foo")
		}
	}

	@Test
	fun doesGivenAccess() {
		script_ {
			number.does { given }
			number(123)
		}.evals_ {
			given { number(123) }
		}
	}

	@Test
	fun doesGivenNumberAccess() {
		script_ {
			number.does { given.number }
			number(123)
		}.evals_ {
			number(123)
		}
	}
}