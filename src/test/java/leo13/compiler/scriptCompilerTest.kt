package leo13.compiler

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.script.lineTo
import leo13.script.script
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.voidProcessor
import kotlin.test.Test

class ScriptCompilerTest {
	@Test
	fun processName() {
		ScriptCompiler(
			errorConverter(),
			voidProcessor(),
			script())
			.process(token(opening("point")))
			.process(token(closing))
			.assertEqualTo(
				ScriptCompiler(
					errorConverter(),
					voidProcessor(),
					script("point")))
	}

	@Test
	fun processLine() {
		ScriptCompiler(
			errorConverter(),
			voidProcessor(),
			script())
			.process(token(opening("point")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				ScriptCompiler(
					errorConverter(),
					voidProcessor(),
					script("point" lineTo script("zero"))))
	}

	@Test
	fun processLines() {
		ScriptCompiler(
			errorConverter(),
			voidProcessor(),
			script())
			.process(token(opening("x")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("y")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				ScriptCompiler(
					errorConverter(),
					voidProcessor(),
					script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
	}
}