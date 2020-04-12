package leo14.untyped.typed.lambda

import leo14.bigDecimal
import leo14.untyped.dsl2.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun literals() {
		script_ { number(10) }.gives_ { number(10) }
		script_ { text("foo") }.gives_ { text("foo") }
	}

	@Test
	fun literalJava() {
		script_ { text("foo").java }.gives_ { java_("foo") }
		script_ { number(123).java }.gives_ { java_(123.bigDecimal) }
	}

	@Test
	fun structs() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.gives_ {
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
		}.gives_ {
			x { number(10) }
		}

		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}.y
		}.gives_ {
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
		}.gives_ {
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
		script_ {
			number(10).type
		}.gives_ {
			number
		}

		script_ {
			text("foo").type
		}.gives_ {
			text
		}

		script_ {
			point {
				x { number(10) }
				y { number(10) }
			}.type
		}.gives_ {
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
		}.gives_ {
			nothing_
		}
	}

	@Test
	fun manyIs() {
		script_ {
			x.is_ { number(123) }
			y.is_ { number(124) }
			z.is_ { number(125) }
		}.gives_ {
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
		}.gives_ {
			number(124)
		}
	}

	@Test
	fun is_staticType() {
		script_ {
			number.is_ { result }
			number
		}.gives_ {
			result
		}

		script_ {
			number.is_ { result }
			number(123)
		}.gives_ {
			number(123)
		}
	}

	@Test
	fun is_dynamicType() {
		script_ {
			number(123).is_ { result }
		}.gives_ {
			number(123).is_ { result }
		}
	}
}