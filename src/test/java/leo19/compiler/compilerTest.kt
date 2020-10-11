package leo19.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo19.term.nullTerm
import leo19.type.fieldTo
import leo19.type.struct
import leo19.typed.of
import kotlin.test.Test

class CompilerTest {
	@Test
	fun empty() {
		script()
			.typed
			.assertEqualTo(nullTerm of struct())
	}

	@Test
	fun name() {
		script("foo")
			.typed
			.assertEqualTo(nullTerm of struct("foo" fieldTo struct()))
	}

	@Test
	fun structScript() {
		script(
			"x" lineTo script(),
			"y" lineTo script())
			.typed
			.assertEqualTo(nullTerm of struct("x" fieldTo struct(), "y" fieldTo struct()))
	}
}