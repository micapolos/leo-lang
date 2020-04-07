package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo14.invoke
import leo14.leo
import leo14.number
import kotlin.test.Test

class ScopeTest {
	@Test
	fun apply() {
		empty.scope.plus(definition(rule(intType, intType) { (it as Int).inc() }))
			.apply(1.valueSelfTyped)
			.typedAssertEqualTo(2.typed)
	}

	@Test
	fun compiled() {
		empty.scope.compiled(leo(2, "plus"(3))).value.assertEqualTo(number(5))
	}
}