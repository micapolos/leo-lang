package leo3

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class FunctionTest {
	@Test
	fun bitNegateFunction() {
		val bitNegateFunction = function(
			token(begin(word("bit"))) to match(
				function(
					token(begin(word("zero"))) to match(
						function(token(end) to match(
							function(token(end) to match(
								function(
									token(begin(word("negate"))) to match(
										function(
											token(end) to match(
												body(function(), template(argument))))))))))),
					token(begin(word("one"))) to match(
						function(token(end) to match(
							function(token(end) to match(
								function(
									token(begin(word("negate"))) to match(
										function(
											token(end) to match(
												body(function(), template(argument))))))))))))))

		value(bitNegateFunction)
			.plus(token(begin(word("bit"))))!!
			.plus(token(begin(word("zero"))))!!
			.plus(token(end))!!
			.plus(token(end))!!
			.plus(token(begin(word("negate"))))!!
			.plus(token(end))!!
			.string
			.assertEqualTo("bit(zero())negate()")
	}
}