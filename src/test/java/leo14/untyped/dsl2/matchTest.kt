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
				equals_ { text("circle") }
			}

			assert {
				shape { square }
				match {
					circle { text("circle") }
					square { text("square") }
				}
				equals_ { text("square") }
			}

			assert {
				shape { circle }
				match {
					circle { text("circle 1") }
					square { text("square") }
					circle { text("circle 2") }
				}
				equals_ { text("circle 2") }
			}

			assert {
				shape { triangle }
				match {
					circle { text("circle") }
					square { text("square") }
				}
				equals_ {
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