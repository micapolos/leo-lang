package leo13.untyped.normalizer

import leo.base.assertEqualTo
import leo13.errorConverter
import leo13.processor
import leo13.token.closing
import leo13.token.opening
import leo13.token.token
import leo9.stack
import kotlin.test.Test

class NormalizerTest {
	@Test
	fun processName() {
		normalizer(errorConverter(), processor())
			.process(token(opening("zero")))
			.process(token(closing))
			.assertEqualTo(
				normalizer(
					errorConverter(),
					processor(
						token(opening("zero")),
						token(closing)),
					processor(),
					stack(
						token(opening("zero")),
						token(closing))))
	}

	@Test
	fun processNormalizing() {
		normalizer(errorConverter(), processor())
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(opening("x")))
			.process(token(closing))
			.assertEqualTo(
				normalizer(
					errorConverter(),
					processor(
						token(opening("x")),
						token(opening("zero")),
						token(closing),
						token(closing)),
					processor(),
					stack(
						token(opening("x")),
						token(opening("zero")),
						token(closing),
						token(closing))))
	}

	@Test
	fun processNotNormalizing() {
		normalizer(errorConverter(), processor())
			.process(token(opening("x")))
			.process(token(opening("zero")))
			.process(token(closing))
			.process(token(closing))
			.process(token(opening("y")))
			.process(token(opening("one")))
			.process(token(closing))
			.process(token(closing))
			.assertEqualTo(
				normalizer(
					errorConverter(),
					processor(
						token(opening("x")),
						token(opening("zero")),
						token(closing),
						token(closing),
						token(opening("y")),
						token(opening("one")),
						token(closing),
						token(closing)),
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
		normalizer(errorConverter(), processor())
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
				normalizer(
					errorConverter(),
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
						token(closing)),
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
}