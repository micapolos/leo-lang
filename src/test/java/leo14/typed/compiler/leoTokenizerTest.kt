package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.*
import leo14.syntax.Syntax
import kotlin.test.Test

class LeoTokenizerTest {
	@Test
	fun tokenize() {
		processorString {
			map(Token::coreString).map(Syntax::token).process(EMPTY_COMPILER.parse(token(begin("foo"))))
		}.assertEqualTo("foo(")
	}
}