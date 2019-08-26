package leo13.token

import leo.base.assertEqualTo
import kotlin.test.Test

class TokenizerTest {
	@Test
	fun writing_emptyChars() {
		val tokenizer = tokenizer().tokenPush(token(opening("foo")))

		tokenizer
			.push('a')
			.assertEqualTo(tokenizer.charPush('a'))

		tokenizer
			.push('(')
			.assertEqualTo(tokenizer.put(leo13.token.error('(')))

		tokenizer
			.push(')')
			.assertEqualTo(tokenizer.tokenPush(token(closing)))
	}

	@Test
	fun writing_someChars() {
		val tokenizer = tokenizer().charPush('a').charPush('b')

		tokenizer
			.push('c')
			.assertEqualTo(tokenizer.charPush('c'))

		tokenizer
			.push('(')
			.assertEqualTo(tokenizer.tokenPush(token(opening("ab"))))

		tokenizer
			.push(')')
			.assertEqualTo(tokenizer.put(leo13.token.error(')')))
	}

	@Test
	fun writing_error() {
		val tokenizer = tokenizer().put(leo13.token.error('('))

		tokenizer
			.push('c')
			.assertEqualTo(tokenizer)

		tokenizer
			.push('(')
			.assertEqualTo(tokenizer)

		tokenizer
			.push(')')
			.assertEqualTo(tokenizer)
	}
}
