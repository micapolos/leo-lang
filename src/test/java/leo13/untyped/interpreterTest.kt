package leo13.untyped

import leo13.errorConverter
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo13.untyped.interpreter.Interpreted
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun process() {
		errorConverter<Interpreted, Token>()
			.interpreter()
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(null)
	}
}