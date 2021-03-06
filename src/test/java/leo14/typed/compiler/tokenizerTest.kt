package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.*
import leo14.syntax.Syntax
import leo14.typed.compiler.natives.emptyCompiler
import kotlin.test.Test

class LeoTokenResolverTest {
	@Test
	fun tokenize() {
		processorString {
			map(Token::coreString).map(Syntax::token).process(emptyCompiler.parse(token(begin("foo"))))
		}.assertEqualTo("foo(")
	}
}