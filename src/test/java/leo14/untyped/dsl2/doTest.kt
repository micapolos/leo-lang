package leo14.untyped.dsl2

import kotlin.test.Test

class DoTest {
	@Test
	fun test_() {
		run_ {
			assert {
				first { name { text("John") } }
				last { name { text("Wayne") } }
				do_ {
					given.first.name.text
					plus { text(" ") }
					plus { given.last.name.text }
				}
				gives { text("John Wayne") }
			}
		}
	}
}