package leo21.evaluated

import leo.base.assertEqualTo
import kotlin.test.Test

class MakeTest {
	@Test
	fun make() {
		evaluated(
			"x" lineTo evaluated(10.0),
			"y" lineTo evaluated(20.0))
			.make("point")
			.assertEqualTo(
				evaluated(
					"point" lineTo evaluated(
						"x" lineTo evaluated(10.0),
						"y" lineTo evaluated(20.0)))
			)
	}
}