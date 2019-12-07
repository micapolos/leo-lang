package leo14.typed.compiler.js

import leo.base.assertEqualTo
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script
import kotlin.test.Test

class ScriptCompileTest {
	@Test
	fun give() {
		script(
			"foo".literal.line,
			"give" lineTo script(
				"given".line,
				"text".line,
				"plus" lineTo script(
					"given".line,
					"text".line)))
			.compile
			.assertEqualTo(
				script("javascript" lineTo script(
					"(v0=>(v0)+(v0))('foo')".literal)))
	}
}