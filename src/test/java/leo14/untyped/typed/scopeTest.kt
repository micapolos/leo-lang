package leo14.untyped.typed

import leo.base.empty
import kotlin.test.Test

class ScopeTest {
	@Test
	fun apply() {
		empty.scope.plus(definition(rule(intType, intType) { (it as Int).inc() }))
			.apply(1.valueSelfTyped)
			.typedAssertEqualTo(2.typed)
	}
}