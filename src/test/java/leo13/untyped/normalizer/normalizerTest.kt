package leo13.untyped.normalizer

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.processor
import leo13.processorStack
import leo13.token.Token
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo9.stack
import kotlin.test.Test

class NormalizerTest {
	@Test
	fun processName() {
		processor<Token>()
			.normalizer(errorConverter())
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(
				processor(
					token(opening("zero")),
					token(closing)).normalizer(
					errorConverter(),
					processor(),
					stack(
						token(opening("zero")),
						token(closing))))
	}

	@Test
	fun processNormalizing() {
		processor<Token>()
			.normalizer(errorConverter())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("x")))
			.process(token(closing))
			.assertEqualTo(
				processor(
					token(opening("x")),
					token(opening("zero")),
					token(closing),
					token(closing))
					.normalizer(
						errorConverter(),
						processor(),
						stack(
							token(opening("x")),
							token(opening("zero")),
							token(closing),
							token(closing))))
	}

	@Test
	fun processNotNormalizing() {
		processor<Token>()
			.normalizer(errorConverter())
			.process(token(opening("x")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("y")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				processor(
					token(opening("x")),
					token(opening("zero")),
					token(closing),
					token(closing),
					token(opening("y")),
					token(opening("one")),
					token(closing),
					token(closing))
					.normalizer(
						errorConverter(),
						processor(),
						stack(
							token(opening("x")),
							token(opening("zero")),
							token(closing),
							token(closing),
							token(opening("y")),
							token(opening("one")),
							token(closing),
							token(closing))))
	}

	@Test
	fun processDeepNormalizing() {
		processor<Token>()
			.normalizer(errorConverter())
			.process(token(opening("x")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("y")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("point")))
			.process(token(closing))
			.assertEqualTo(
				processor(
					token(opening("point")),
					token(opening("x")),
					token(opening("zero")),
					token(closing),
					token(closing),
					token(opening("y")),
					token(opening("one")),
					token(closing),
					token(closing),
					token(closing))
					.normalizer(
						errorConverter(),
						processor(),
						stack(
							token(opening("point")),
							token(opening("x")),
							token(opening("zero")),
							token(closing),
							token(closing),
							token(opening("y")),
							token(opening("one")),
							token(closing),
							token(closing),
							token(closing))))
	}

	@Test
	fun endToken() {
		processorStack<Token> {
			normalizer()
				.process(token(opening("zero")))
				.process(token(closing))
				.process(token(closing))
		}.assertEqualTo(
			stack(
				token(opening("zero")),
				token(closing),
				token(closing)))
	}
}