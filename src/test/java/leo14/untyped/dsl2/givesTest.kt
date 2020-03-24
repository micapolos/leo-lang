package leo14.untyped.dsl2

import kotlin.test.Test

class GivesTest {
	@Test
	fun test_() {
		run_ {
			x.gives { number(10) }
			assert {
				x.gives { number(10) }
				y.gives { y }
			}

			y.gives { number(20) }
			assert {
				x.gives { number(10) }
				y.gives { number(20) }
			}

			x.gives { x.plus { y } }
			assert {
				x.gives { number(30) }
				y.gives { number(20) }
			}
		}
	}
}