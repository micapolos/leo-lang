package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import org.junit.Test

class EvaluatorTest {
	@Test
	fun quoteUnquote() {
		context()
			.environment
			.quote
			.unquoteOrNull!!
			.assertEqualTo(context().environment)
	}

	@Test
	fun write_context() {
		context()
			.environment
			.evaluator(thunk(value(line(literal(5)))))
			.write(minusName lineTo value(line(literal(3))))
			.assertEqualTo(
				context()
					.environment
					.evaluator(thunk(value(line(literal(2))))))
	}

	@Test
	fun write_quoted() {
		context()
			.environment
			.quote
			.evaluator(thunk(value(line(literal(5)))))
			.write(minusName lineTo value(line(literal(3))))
			.assertEqualTo(
				context()
					.environment
					.quote
					.evaluator(
						thunk(
							value(
								line(literal(5)),
								minusName lineTo value(line(literal(3)))))))
	}
}