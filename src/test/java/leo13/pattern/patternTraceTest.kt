package leo13.pattern

import leo.base.assertEqualTo
import kotlin.test.Test
import kotlin.test.assertFails

class PatternTraceTest {
	@Test
	fun plusRecurseOrNull() {
		val trace0 = trace(node("zero" lineTo pattern()))
		val trace1 = trace0.plus(node("one" lineTo pattern()))
		val trace2 = trace1.plus(node("two" lineTo pattern()))

		trace2.plus(recurse).assertEqualTo(trace2)
		trace2.plus(recurse.recurse).assertEqualTo(trace1)
		trace2.plus(recurse.recurse.recurse).assertEqualTo(trace0)
		assertFails { trace2.plus(recurse.recurse.recurse.recurse) }
	}

	@Test
	fun plusPatternOrNull() {
		val trace = trace(node("zero" lineTo pattern()))

		trace
			.plus(pattern(node("one" lineTo pattern())))
			.assertEqualTo(trace.plus(node("one" lineTo pattern())))

		trace
			.plus(pattern(recurse))
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

	@Test
	fun linesContains() {
		val trace = trace(
			node(
				"x" lineTo pattern("zero"),
				"y" lineTo pattern("one")))

		trace
			.contains(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one")))
			.assertEqualTo(true)

		trace
			.contains(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("two")))
			.assertEqualTo(false)

		trace
			.contains(
				pattern(
					"y" lineTo pattern("one"),
					"x" lineTo pattern("zero")))
			.assertEqualTo(false)

		trace
			.contains(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one"),
					"z" lineTo pattern("two")))
			.assertEqualTo(false)
	}

	@Test
	fun choiceContains() {
		val trace = trace(node(options("zero", "one")))

		trace
			.contains(pattern("zero"))
			.assertEqualTo(true)

		trace
			.contains(pattern("one"))
			.assertEqualTo(true)

		trace
			.contains(pattern("two"))
			.assertEqualTo(false)

		trace
			.contains(pattern(trace.node))
			.assertEqualTo(true)
	}

	@Test
	fun recurseContains() {
		val trace = trace(
			node(
				"list" lineTo pattern(
					options(
						"empty" lineTo pattern(),
						"link" lineTo pattern(recurse.recurse).plus("unit" lineTo pattern())))))

		trace
			.contains(pattern("list" lineTo pattern("empty")))
			.assertEqualTo(true)

		trace
			.contains(
				pattern(
					"list" lineTo pattern(
						"link" lineTo pattern(
							"list" lineTo pattern("empty"),
							"unit" lineTo pattern()))))
			.assertEqualTo(true)

		trace
			.contains(
				pattern(
					"list" lineTo pattern(
						"link" lineTo pattern(
							"list" lineTo pattern(
								"link" lineTo pattern(
									"list" lineTo pattern("empty"),
									"unit" lineTo pattern())),
							"unit" lineTo pattern()))))
			.assertEqualTo(true)

		trace
			.contains(pattern(trace.node))
			.assertEqualTo(true)
	}
}
