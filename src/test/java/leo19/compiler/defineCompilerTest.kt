package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.type.struct
import leo19.typed.typed
import kotlin.test.Test

class DefineCompilerTest {
	@Test
	fun defineIs() {
		DefineCompiler(
			emptyContext,
			struct("zero"))
			.plus(script("is" lineTo script("one")))
			.assertEqualTo(DefineCompiler(emptyContext.defineIs(struct("zero"), typed("one")), struct()))
	}
}