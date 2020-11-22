package leo21.evaluated

import leo.base.assertEqualTo
import kotlin.test.Test

class GetTest {
	@Test
	fun struct_1() {
		evaluated(
			"foo" lineTo evaluated(
				"Hello, world!".lineEvaluated,
				10.0.lineEvaluated))
			.getOrNull("text")
			.assertEqualTo(evaluated("Hello, world!"))
	}

	@Test
	fun struct_2() {
		evaluated(
			"foo" lineTo evaluated(
				"Hello, world!".lineEvaluated,
				10.0.lineEvaluated))
			.getOrNull("number")
			.assertEqualTo(evaluated(10.0))
	}
}