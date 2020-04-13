package leo14.untyped.typed.lambda

import leo14.untyped.dsl2.*
import org.junit.Test

class CompileTypeTest {
	@Test
	fun literal() {
		script_ { number(10) }.compilesType_ { number }
		script_ { text("foo") }.compilesType_ { text }
	}

	@Test
	fun literalJava() {
		script_ { number(10).java }.compilesType_ { java }
		script_ { text("foo").java }.compilesType_ { java }
	}

	@Test
	fun struct() {
		script_ {
			point {
				x { number(10) }
				y { number(20) }
			}
		}.compilesType_ {
			point {
				x { number }
				y { number }
			}
		}
	}
}