package leo14.untyped.dsl2

import kotlin.test.Test

class NumberTest {
	@Test
	fun test_() {
		run_ {
			assert {
				number(2)
				plus { number(3) }
				equals_ { number(5) }
			}

			assert {
				number(5)
				minus { number(3) }
				equals_ { number(2) }
			}

			assert {
				number(2)
				times { number(3) }
				equals_ { number(6) }
			}

			assert {
				minus { number(2) }
				equals_ { number(-2) }
			}
		}
	}
}
