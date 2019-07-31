package leo9

import leo.base.assertEqualTo
import leo.base.empty
import kotlin.test.Test

class FunctionTest {
	@Test
	fun call() {
		function(empty)
			.call(script("one" lineTo script()))
			.assertEqualTo(script())

		function(argument())
			.call(script("one" lineTo script()))
			.assertEqualTo(script("one" lineTo script()))
	}
}
