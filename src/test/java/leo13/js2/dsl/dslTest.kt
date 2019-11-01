package leo13.js2.dsl

import leo.base.assertEqualTo
import leo13.js2.*
import kotlin.test.Test

class DslTest {
	@Test
	fun code() {
		seq(
			window.onload.set(fn() ret console.log(expr(1.0))),
			window.onkeypress.set(fn("event") ret console.log(event))
		).code.assertEqualTo(null)
	}
}