package leo20.tokenizer

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class EvalTest {
	@Test
	fun struct() {
		script(
			"point" lineTo script(
				"x" lineTo script(literal(10.0)),
				"y" lineTo script(literal(20.0))),
			"x" lineTo script())
			.eval
			.assertEqualTo(script("x" lineTo script(literal(10.0))))
	}
}