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
			.evaluator(program(line(literal(5))))
			.write(minusName lineTo program(line(literal(3))))
			.assertEqualTo(
				context()
					.environment
					.evaluator(
						program(line(literal(2)))))
	}

	@Test
	fun write_quoted() {
		context()
			.environment
			.quote
			.evaluator(program(line(literal(5))))
			.write(minusName lineTo program(line(literal(3))))
			.assertEqualTo(
				context()
					.environment
					.quote
					.evaluator(
						program(
							line(literal(5)),
							minusName lineTo program(line(literal(3))))))
	}
}