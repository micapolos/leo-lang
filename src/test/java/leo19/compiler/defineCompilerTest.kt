package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.type.type
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
					.defineIs(type("zero"), typed("one"))
					.defineCompiler(type()))
	}

	@Test
	fun defineGives() {
		emptyDefineCompiler
			.plus(
				script(
					"zero" lineTo script(),
					"does" lineTo script("one")))
			.assertEqualTo(
				emptyContext
					.defineGives(type("zero"), typed("one"))
					.defineCompiler(type()))
	}
}