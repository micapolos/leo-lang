package leo14.untyped.dsl2

import kotlin.test.Test

class ItTest {
	@Test
	fun test_() {
		run_ {
			assert {
				zero.it
				gives { zero.it }
			}

			assert {
				zero
				it { one }
				gives {
					quote {
						zero
						one
					}
				}
			}

			assert {
				zero
				it {
					x
					y
				}
				gives {
					zero
					it {
						x
						y
					}
				}
			}

			assert {
				zero
				it {
					number(2)
					plus { number(3) }
				}
				gives {
					zero
					number(5)
				}
			}
		}
	}
}