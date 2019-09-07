package leo13.base

import leo.base.assertEqualTo
import leo13.base.typed.typed
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test

class TypedTest {
	@Test
	fun boolean() {
		typed(true)
			.typedScriptLine
			.assertEqualTo("boolean" lineTo script("true"))

		typed(false)
			.typedScriptLine
			.assertEqualTo("boolean" lineTo script("false"))
	}
}