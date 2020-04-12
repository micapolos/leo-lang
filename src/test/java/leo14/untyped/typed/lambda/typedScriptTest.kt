package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class TypedScriptTest {
	@Test
	fun literals() {
		12.typed.script.assertEqualTo(leo(12))
		"foo".typed.script.assertEqualTo(leo("foo"))
		typed("foo").script.assertEqualTo(leo("foo"()))
	}
}