package leo14.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class UtilTest {
	@Test
	fun boolean() {
		term<Any>(false).booleanOrNull().assertEqualTo(false)
		term<Any>(true).booleanOrNull().assertEqualTo(true)
		term<Any>(0).booleanOrNull().assertNull
	}
}