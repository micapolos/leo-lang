package leo14.untyped.dsl2

import kotlin.test.Test

class MatchTest {
	@Test
	fun test_() {
		run_ {
			assert {
				shape { circle }
				match {
					circle { text("circle") }
					square { text("square") }
				}
				gives { text("circle") }
			}

			assert {
				shape { square }
				match {
					circle { text("circle") }
					square { text("square") }
				}
				gives { text("square") }
			}

			assert {
				shape { circle }
				match {
					circle { text("circle 1") }
					square { text("square") }
					circle { text("circle 2") }
				}
				gives { text("circle 2") }
			}

			assert {
				shape { triangle }
				match {
					circle { text("circle") }
					square { text("square") }
				}
				gives {
					quote {
						shape { triangle }
						match {
							circle { text("circle") }
							square { text("square") }
						}
					}
				}
			}

			// Implement "anything" case
		}
	}
}