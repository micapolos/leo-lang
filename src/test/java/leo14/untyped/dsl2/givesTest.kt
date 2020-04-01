package leo14.untyped.dsl2

import kotlin.test.Test

class GivesTest {
	@Test
	fun test_() {
		run_ {
			x.is_ { number(10) }
			assert {
				x.is_ { number(10) }
				y.gives { y }
			}

			y.is_ { number(20) }
			assert {
				x.is_ { number(10) }
				y.gives { number(20) }
			}

			x.is_ { x.plus { y } }
			assert {
				x.is_ { number(30) }
				y.gives { number(20) }
			}
		}
	}
}