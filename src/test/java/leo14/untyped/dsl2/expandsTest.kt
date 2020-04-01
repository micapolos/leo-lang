package leo14.untyped.dsl2

import kotlin.test.Test

class WritesTest {
	@Test
	fun test_() {
		run_ {
			assert {
				anything
				def { anything }
				expands {
					script.previous.script.object_
					quote {
						does {
							unquote { script.def.object_ }
						}
					}
				}
				x.def { number(0) }
				x.gives { number(0) }
			}
		}
	}
}