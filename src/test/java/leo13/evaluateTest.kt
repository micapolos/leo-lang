package leo13

import leo.base.assertEqualTo
import leo.java.lang.useResourceCharSeq
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun evaluate() {
		this::class
			.java
			.classLoader
			.useResourceCharSeq("leo/leo13/point.leo") { charEvaluateScriptLine }
			.assertEqualTo(
				okName lineTo script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
	}
}
