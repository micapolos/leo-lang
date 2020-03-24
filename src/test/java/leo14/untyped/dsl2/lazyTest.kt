package leo14.untyped.dsl2

import kotlin.test.Test

class LazyTest {
	@Test
	fun test_() {
		run_ {
			assert {
				lazy_ {
					number(1)
					plus { number(2) }
				}
				script
				gives {
					quote {
						lazy_ {
							number(1)
							plus { number(2) }
						}
					}
				}
			}

			assert {
				lazy_ {
					number(1)
					plus { number(2) }
				}
				force
				gives { number(3) }
			}

			assert {
				lazy_ {
					number(1)
					plus { number(2) }
				}
				script
				force
				gives {
					quote {
						lazy_ {
							number(1)
							plus { number(2) }
						}
					}
				}
			}

			assert {
				lazy_ {
					number(1)
					plus { number(2) }
				}
				plus { number(3) }
				gives { number(6) }
			}
		}
	}
}