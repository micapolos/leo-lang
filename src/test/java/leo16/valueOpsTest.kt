package leo16

import leo.base.assertEqualTo
import leo.base.assertNull
import leo14.bigDecimal
import leo16.names.*
import org.junit.Test

class ValueOpsTest {
	@Test
	fun getOrNull() {
		value(_point(_x(_zero()), _y(_one()))).getOrNull(_x).assertEqualTo(value(_x(_zero())))
		value(_point(_x(_zero()), _y(_one()))).getOrNull(_y).assertEqualTo(value(_y(_one())))
		value(_point(_x(_zero()), _y(_one()))).getOrNull(_z).assertNull

		"123".value.getOrNull(_native).assertEqualTo("123".nativeValue)
		123.bigDecimal.value.getOrNull(_native).assertEqualTo(123.bigDecimal.nativeValue)
		value(_x(value(_zero()).functionTo(value(_one()).compiled).value))
			.getOrNull(_function)
			.assertEqualTo(value(_zero()).functionTo(value(_one()).compiled).value)
	}
}