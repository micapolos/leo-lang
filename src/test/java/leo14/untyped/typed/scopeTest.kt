package leo14.untyped.typed

import kotlin.test.Test

class ScopeTest {
	@Test
	fun apply() {
		scope(definition(rule(intType, intType) { (it as Int).inc() }))
			.apply(1.valueSelfTyped)
			.typedAssertEqualTo(2.typed)
	}
}