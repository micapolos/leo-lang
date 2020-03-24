package leo14.untyped.dsl2

import kotlin.test.Test

class DoTest {
	@Test
	fun test_() {
		run_ {
			assert {
				number(10)
				do_ { text("hello") }
				gives { text("hello") }
			}

			assert {
				number(10)
				do_ { given.number.plus { number(1) } }
				gives { number(11) }
			}
		}
	}
}