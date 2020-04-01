package leo14.untyped.dsl2

import kotlin.test.Test

class GivesTest {
	@Test
	fun test_() {
		run_ {
			x.is_ { number(10) }
			assert {
				x.is_ { number(10) }
				y.equals_ { y }
			}

			y.is_ { number(20) }
			assert {
				x.is_ { number(10) }
				y.equals_ { number(20) }
			}

			x.is_ { x.plus { y } }
			assert {
				x.is_ { number(30) }
				y.equals_ { number(20) }
			}
		}
	}
}