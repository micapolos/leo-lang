package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.number
import kotlin.test.Test

class CastTest {
	@Test
	fun cast() {
		1.valueSelfTyped.cast(self(1)).typedAssertEqualTo(1.valueSelfTyped)
		1.valueSelfTyped.cast(self(2)).assertEqualTo(null)

		1.valueSelfTyped.cast(intType).typedAssertEqualTo(1.typed)
		1.valueSelfTyped.cast(numberType).typedAssertEqualTo(number(1).typed)
		1.valueSelfTyped.cast(textType).assertEqualTo(null)

		"foo".valueSelfTyped.cast(self("foo")).typedAssertEqualTo("foo".valueSelfTyped)
		"foo".valueSelfTyped.cast(self("bar")).assertEqualTo(null)

		"foo".valueSelfTyped.cast(textType).typedAssertEqualTo("foo".typed)
		"foo".valueSelfTyped.cast(intType).assertEqualTo(null)
	}
}