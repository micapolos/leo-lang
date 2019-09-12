package leo13.untyped.evaluator

import leo.base.assertEqualTo
import kotlin.test.Test

class EvaluatorTest {
	@Test
	fun word() {
		evaluator()
			.begin("zero")
			.end!!
			.assertEqualTo(evaluator(context(), value("zero" lineTo value())))
	}

	@Test
	fun line() {
		evaluator()
			.begin("x")
			.begin("zero")!!
			.end!!
			.end!!
			.assertEqualTo(evaluator(context(), value("x" lineTo value("zero"))))
	}

	@Test
	fun lines() {
		evaluator()
			.begin("x")
			.begin("zero")!!
			.end!!
			.end!!
			.begin("y")!!
			.begin("one")!!
			.end!!
			.end!!
			.assertEqualTo(
				evaluator(
					context(),
					value(
						"x" lineTo value("zero"),
						"y" lineTo value("one"))))
	}

	@Test
	fun get() {
		evaluator()
			.begin("point")
			.begin("x")!!
			.begin("zero")!!
			.end!!
			.end!!
			.begin("y")!!
			.begin("one")!!
			.end!!
			.end!!
			.end!!
			.begin("get")!!
			.begin("x")!!
			.end!!
			.end!!
			.assertEqualTo(
				evaluator(
					context(),
					value("x" lineTo value("zero"))))
	}

	@Test
	fun set() {
		evaluator()
			.begin("point")
			.begin("x")!!
			.begin("zero")!!
			.end!!
			.end!!
			.begin("y")!!
			.begin("one")!!
			.end!!
			.end!!
			.end!!
			.begin("set")!!
			.begin("x")!!
			.begin("two")!!
			.end!!
			.end!!
			.end!!
			.assertEqualTo(
				evaluator(
					context(),
					value("point" lineTo value(
						"x" lineTo value("two"),
						"y" lineTo value("one")))))
	}
}