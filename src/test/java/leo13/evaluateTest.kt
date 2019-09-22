package leo13

import leo.base.assertEqualTo
import leo.java.lang.useResourceCharSeq
import kotlin.test.Test

class EvaluateTest {
	@Test
	fun evaluate() {
		this::class.java.classLoader
			.useResourceCharSeq("leo/leo13/point.leo") { charEvaluateScriptLine }
			.assertEqualTo(null)
	}
}
