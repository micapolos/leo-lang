package leo3

import leo.base.assertEqualTo
import leo.base.string
import kotlin.test.Test

class FunctionTest {
	@Test
	fun bitNegateFunction() {
		val cont = function(token(end) to match(
			function(token(end) to match(
				function(
					token(begin(word("negate"))) to match(
						function(
							token(end) to match(template(argument)))))))))

		val bitNegateFunction = function(
			token(begin(word("bit"))) to match(
				function(
					token(begin(word("zero"))) to match(cont),
					token(begin(word("one"))) to match(cont))))

		invocation(bitNegateFunction)
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