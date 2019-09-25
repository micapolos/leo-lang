package leo13.interpreter

import leo13.errorConverter
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import kotlin.test.Test

class InterpreterTest {
	@Test
	fun process() {
		errorConverter<ValueTyped, Token>()
			.interpreter()
			.process(token(opening("foo")))
			.process(token(closing))
			.process(token(opening("interpreter")))
			.process(token(closing))
	}
}