package leo14.untyped.dsl2

import kotlin.test.Test

class QuoteTest {
	@Test
	fun test_() {
		run_ {
			assert {
				quote {
					number(1)
					plus { number(2) }
				}
				gives {
					number(3)
				}
			}
		}
	}
}