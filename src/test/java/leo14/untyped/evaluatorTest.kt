package leo14.untyped

import leo.base.assertEqualTo
import leo14.literal
import org.junit.Test

class EvaluatorTest {
	@Test
	fun quoteUnquote() {
		scope()
			.environment
			.quote
			.unquoteOrNull!!
			.assertEqualTo(scope().environment)
	}

	@Test
	fun write_scope() {
		scope()
			.environment
			.evaluator(thunk(value(line(literal(5)))))
			.write(minusName lineTo value(line(literal(3))))
			.assertEqualTo(
				scope()
					.environment
					.evaluator(thunk(value(line(literal(2))))))
	}

	@Test
	fun write_quoted() {
		scope()
			.environment
			.quote
			.evaluator(thunk(value(line(literal(5)))))
			.write(minusName lineTo value(line(literal(3))))
			.assertEqualTo(
				scope()
					.environment
					.quote
					.evaluator(
						thunk(
							value(
								line(literal(5)),
								minusName lineTo value(line(literal(3)))))))
	}
}