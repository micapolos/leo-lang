package leo13.untyped

import leo.base.assertEqualTo
import leo13.script.lineTo
import leo13.script.script
import leo13.script.scriptLine
import leo13.script.unsafeValue
import kotlin.test.Test

class ContextTest {
	@Test
	fun reader() {
		contextReader
			.unsafeValue(
				"context" lineTo script(
					"function" lineTo script("list"),
					"binding" lineTo script("list")))
			.assertEqualTo(context())
	}

	@Test
	fun writer() {
		contextWriter
			.scriptLine(context())
			.assertEqualTo(
				"context" lineTo script(
					"function" lineTo script("list"),
					"binding" lineTo script("list")))
	}
}