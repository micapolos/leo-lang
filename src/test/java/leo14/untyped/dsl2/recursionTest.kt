package leo14.untyped.dsl2

import kotlin.test.Test

class RecursionTest {
	@Test
	fun test_() {
		run_ {
			function { recursive { zero { lazy_ { do_ { recurse } } } } }
			apply
			print
		}
	}
}