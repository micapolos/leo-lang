package leo14.untyped.typed

import kotlin.test.Test

class RuleTest {
	@Test
	fun applySelfTyped() {
		rule(intType, intType) { (it as Int).inc() }
			.apply(1.valueSelfTyped)
			.typedAssertEqualTo(2.typed)
	}

	@Test
	fun applyTyped() {
		rule(intType, intType) { (it as Int).inc() }
			.apply(1.typed)
			.typedAssertEqualTo(2.typed)
	}
}