package leo14.untyped.dsl2

import kotlin.test.Test

class ItTest {
	@Test
	fun test_() {
		run_ {
			assert {
				zero.it
				equals_ { zero.it }
			}

			assert {
				zero
				it { one }
				equals_ {
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
				equals_ {
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
				equals_ {
					zero
					number(5)
				}
			}
		}
	}
}