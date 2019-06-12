package leo5.script

import leo.base.assertEqualTo
import kotlin.test.Test

class WritingTest {
	@Test
	fun writing() {
		writeScript {
			span("bit", span("zero"))
			span("plus", span("bit", span("one")), span("negate"))
		}.assertEqualTo(
			script(
				"bit" lineTo script("zero"),
				"plus" lineTo script(
					"bit" lineTo script("one"),
					"negate" lineTo script())))
	}
}
