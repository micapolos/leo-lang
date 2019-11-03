package leo13.lambda

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class UtilTest {
	@Test
	fun boolean() {
		value<Any>(false).booleanOrNull().assertEqualTo(false)
		value<Any>(true).booleanOrNull().assertEqualTo(true)
		value<Any>(0).booleanOrNull().assertNull
	}
}