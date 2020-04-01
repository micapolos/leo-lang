package leo14.untyped.dsl2

import kotlin.test.Test

class AsTest {
	@Test
	fun test_() {
		run_ {
			number(10).as_ { x }
			assert {
				x.is_ { number(10) }
				y.gives { y }
			}

			number(20).as_ { y }
			assert {
				x.is_ { number(10) }
				y.gives { number(20) }
			}

			x
			plus { number(30) }
			as_ { x }
			assert {
				x.is_ { number(40) }
				y.gives { number(20) }
			}
		}
	}
}