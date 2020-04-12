package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.leo
import leo14.untyped.isName
import leo14.untyped.typed.type
import org.junit.Test

class CompilerTest {
	@Test
	fun typeIsTyped() {
		emptyLibrary
			.compiler(typed("x"))
			.plus(isName fieldTo leo(123))
			.assertEqualTo(emptyLibrary.plus(type("x") entryTo 123.typed).compiler(emptyTyped))
	}
}