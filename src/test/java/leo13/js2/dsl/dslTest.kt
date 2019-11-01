package leo13.js2.dsl

import leo13.js2.*
import kotlin.test.Test

class DslTest {
	@Test
	fun code() {
		block(
			window.onload set fn().does(console.log(expr(1.0)).run),
			window.onkeypress set fn("event").does(console.log(event).run)
		).code
	}
}