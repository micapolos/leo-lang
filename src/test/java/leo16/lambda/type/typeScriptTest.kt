package leo16.lambda.type

import leo.base.assertEqualTo
import leo14.emptyScript
import kotlin.test.Test

class TypeScriptTest {
	@Test
	fun empty() {
		emptyType.script.assertEqualTo(emptyScript)
	}
}