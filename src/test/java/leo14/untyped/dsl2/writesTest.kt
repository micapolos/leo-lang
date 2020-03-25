package leo14.untyped.dsl2

import kotlin.test.Test

class WritesTest {
	@Test
	fun test_() {
		run_ {
			assert {
				anything
				def { anything }
				writes {
					given.previous.given.content
					quote {
						gives {
							unquote { given.def.content }
						}
					}
				}
				x.def { number(0) }
				x.gives { number(0) }
			}
		}
	}
}