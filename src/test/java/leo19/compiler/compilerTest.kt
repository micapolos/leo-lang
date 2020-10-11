package leo19.compiler

import leo.base.assertEqualTo
import leo14.script
import leo19.term.term
import leo19.type.struct
import leo19.type.fieldTo
import kotlin.test.Test

class CompilerTest {
	@Test
	fun name() {
		script("foo")
			.typed
			.assertEqualTo(term() of struct("foo" fieldTo struct()))
	}
}