package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.*
import kotlin.test.Test

class LeoTokenizerTest {
	@Test
	fun tokenize() {
		processorString {
			map<String, Token> { it.toString() }.process(emptyLeo.parse(token(begin("foo"))))
		}.assertEqualTo("foo(")
	}
}