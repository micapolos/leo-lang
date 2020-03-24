package leo14.untyped.dsl2

import kotlin.test.Test

class ScriptTest {
	@Test
	fun test_() {
		run_ {
			assert {
				function { x }
				script
				apply
				gives {
					quote {
						function { x }
						apply
					}
				}
			}
		}
	}
}