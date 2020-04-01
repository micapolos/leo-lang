package leo14.untyped.dsl2

import kotlin.test.Test

class DoesTest {
	@Test
	fun test_() {
		run_ {
			text
			plus { attribute { text } }
			does {
				text
				plus { plus.attribute.text }
			}

			assert {
				text("<")
				plus { attribute { text("href") } }
				equals_ { text("<href") }
			}
		}
	}
}