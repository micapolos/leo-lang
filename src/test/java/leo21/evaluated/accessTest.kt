package leo21.evaluated

import leo.base.assertEqualTo
import leo.base.assertNull
import leo21.evaluator.accessOrNull
import leo21.evaluator.evaluated
import leo21.evaluator.lineTo
import kotlin.test.Test

class AccessTest {
	@Test
	fun struct_match1() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.accessOrNull("x")
			.assertEqualTo(evaluated("x" lineTo evaluated(10.0)))
	}

	@Test
	fun struct_match2() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.accessOrNull("y")
			.assertEqualTo(evaluated("y" lineTo evaluated(20.0)))
	}

	@Test
	fun struct_mismatch() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.accessOrNull("z")
			.assertNull
	}
}