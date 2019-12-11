package leo14.lib

import leo.base.assertEqualTo
import kotlin.test.Test

class NumberTest {
	@Test
	fun math() {
		10.number.decrement.assertEqualTo(9.number)
		10.number.increment.assertEqualTo(11.number)
		(10.number + 3.number).assertEqualTo(13.number)
		(10.number - 3.number).assertEqualTo(7.number)
		(10.number * 3.number).assertEqualTo(30.number)
	}
}