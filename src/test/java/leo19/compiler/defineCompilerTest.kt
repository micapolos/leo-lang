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
		emptyDefineCompiler
			.plus(
				script(
					"zero" lineTo script(),
					"is" lineTo script("one")))
			.assertEqualTo(
				emptyContext
					.defineIs(struct("zero"), typed("one"))
					.defineCompiler(struct()))
	}

	@Test
	fun defineGives() {
		emptyDefineCompiler
			.plus(
				script(
					"zero" lineTo script(),
					"gives" lineTo script("one")))
			.assertEqualTo(
				emptyContext
					.defineGives(struct("zero"), typed("one"))
					.defineCompiler(struct()))
	}
}