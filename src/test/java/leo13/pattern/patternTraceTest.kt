package leo13.pattern

import leo.base.assertEqualTo
import kotlin.test.Test

class PatternTraceTest {
	@Test
	fun plusRecurseOrNull() {
		val trace0 = trace(node("zero" lineTo pattern()))
		val trace1 = trace0.plus(node("one" lineTo pattern()))
		val trace2 = trace1.plus(node("two" lineTo pattern()))

		trace2.plusOrNull(recurse).assertEqualTo(trace2)
		trace2.plusOrNull(recurse.recurse).assertEqualTo(trace1)
		trace2.plusOrNull(recurse.recurse.recurse).assertEqualTo(trace0)
		trace2.plusOrNull(recurse.recurse.recurse.recurse).assertEqualTo(null)
	}

	@Test
	fun plusPatternOrNull() {
		val trace = trace(node("zero" lineTo pattern()))

		trace
			.plusOrNull(pattern(node("one" lineTo pattern())))
			.assertEqualTo(trace.plus(node("one" lineTo pattern())))

		trace
			.plusOrNull(pattern(recurse))
			.assertEqualTo(trace)
	}

	@Test
	fun plusLine() {
		trace(node("zero" lineTo pattern()))
			.plus("one" lineTo pattern())
			.assertEqualTo(
				trace(
					node(
						"zero" lineTo pattern(),
						"one" lineTo pattern())))
	}

	@Test
	fun previousOrNull() {
		trace(
			node(
				"zero" lineTo pattern(),
				"one" lineTo pattern()))
			.previousOrNull
			.assertEqualTo(trace(node("zero" lineTo pattern())))
	}

	@Test
	fun contentOrNull() {
		val contentNode =
			node(
				"x" lineTo pattern("zero"),
				"y" lineTo pattern("one"))

		val trace = trace(node("point" lineTo pattern(contentNode)))

		trace
			.contentOrNull
			.assertEqualTo(trace.plus(contentNode))
	}

	@Test
	fun getOrNull() {
		val trace = trace(
			node(
				"point" lineTo pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one"))))

		trace
			.getOrNull("x")
			.assertEqualTo(trace.plus(node("x" lineTo pattern("zero"))))

		trace
			.getOrNull("y")
			.assertEqualTo(trace.plus(node("y" lineTo pattern("one"))))

		trace
			.getOrNull("z")
			.assertEqualTo(null)
	}
}
