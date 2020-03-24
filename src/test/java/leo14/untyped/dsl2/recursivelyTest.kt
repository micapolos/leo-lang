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
				text("")
				dot { number(10) }
				recursively {
					do_ {
						given.dot.number
						equals_ { number(0) }
						match {
							true_ { given.text }
							false_ {
								given.text
								plus { text(".") }
								dot {
									given.dot.number
									minus { number(1) }
								}
								recurse
							}
						}
					}
				}
				gives { text("..........") }
			}
		}
	}
}