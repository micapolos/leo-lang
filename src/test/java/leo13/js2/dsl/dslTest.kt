package leo13.js2.dsl

import leo.base.assertEqualTo
import leo13.js2.code
import leo13.js2.expr
import leo13.js2.invoke
import leo13.js2.set
import kotlin.test.Test

class DslTest {
	@Test
	fun code() {
		window
			.onload
			.set(function().returns(
				console.log(expr(1.0)),
				console.log(expr("jajeczko"))
			))
			.code
			.assertEqualTo(null)
	}
}