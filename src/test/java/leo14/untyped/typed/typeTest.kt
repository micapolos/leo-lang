package leo14.untyped.typed

import kotlin.test.Test

class TypeTest {
	@Test
	fun cast() {
		1.valueSelfTyped.cast(intType).typedAssertEqualTo(1.typed)
	}
}