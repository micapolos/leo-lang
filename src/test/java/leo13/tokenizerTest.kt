package leo13

import leo.base.assertEqualTo
import leo13.script.push
import leo13.script.tokenizer
import leo9.push
import leo9.stack
import kotlin.test.Test

class TokenizerTest {
	@Test
	fun writing() {
		val tokenStack = stack(token(opening("foo")))

		tokenizer(tokenStack, stack())
			.push('a')
			.assertEqualTo(tokenizer(tokenStack, stack('a')))

		tokenizer(tokenStack, stack())
			.push('(')
			.assertEqualTo(null)

		tokenizer(tokenStack, stack())
			.push(')')
			.assertEqualTo(tokenizer(tokenStack.push(token(closing)), stack()))

		tokenizer(tokenStack, stack('a'))
			.push('b')
			.assertEqualTo(tokenizer(tokenStack, stack('a', 'b')))

		tokenizer(tokenStack, stack('a'))
			.push('(')
			.assertEqualTo(tokenizer(tokenStack.push(token(opening("a"))), stack()))

		tokenizer(tokenStack, stack('a'))
			.push(')')
			.assertEqualTo(null)

		tokenizer(tokenStack, stack('a', 'b'))
			.push('c')
			.assertEqualTo(tokenizer(tokenStack, stack('a', 'b', 'c')))

		tokenizer(tokenStack, stack('a', 'b'))
			.push('(')
			.assertEqualTo(tokenizer(tokenStack.push(token(opening("ab"))), stack()))

		tokenizer(tokenStack, stack('a', 'b'))
			.push(')')
			.assertEqualTo(null)
	}
}