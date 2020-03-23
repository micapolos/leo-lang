package leo14.untyped.dsl2

import kotlin.test.Test

class TextTest {
	@Test
	fun _test() {
		_run {
			assert {
				text("Hello, ")
				plus { text("world!") }
				gives { text("Hello, world!") }
			}
		}
	}
}