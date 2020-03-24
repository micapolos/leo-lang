package leo14.untyped.dsl2

import kotlin.test.Test

class RecursivelyTest {
	@Test
	fun test_() {
		run_ {
			assert {
				number(10)
				recursively {
					minus { number(1) }
				}
				gives { number(9) }
			}

			assert {
				recursively {
					ping.gives { pong }
				}
				ping.gives { pong }
			}

			assert {
				boolean { false_ }
				recursively {
					match {
						true_ { done }
						false_ { boolean { true_ }.recurse }
					}
				}
				gives { done }
			}

			assert {
				number(6)
				recursively {
					do_ {
						given.number
						equals_ { number(1) }
						match {
							true_ { given.number }
							false_ {
								given.number
								minus { number(1) }
								recurse
								times { given.number }
							}
						}
					}
				}
				gives { number(720) }
			}
		}
	}
}