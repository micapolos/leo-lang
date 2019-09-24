package leo13.pattern

import leo.base.assertEqualTo
import leo13.compiler.nameTrace
import leo13.script.lineTo
import leo13.script.script
import kotlin.test.Test
import kotlin.test.assertFails

class PatternTest {
	@Test
	fun scriptLine() {
		pattern()
			.scriptingLine
			.assertEqualTo("pattern" lineTo script())

		pattern("zero")
			.scriptingLine
			.assertEqualTo("pattern" lineTo script("zero"))

		pattern(
			"zero" lineTo pattern(),
			"plus" lineTo pattern("one"))
			.scriptingLine
			.assertEqualTo("pattern" lineTo script(
				"zero" lineTo script(),
				"plus" lineTo script("one")))

		pattern(
			options(
				"zero" lineTo pattern(),
				"one" lineTo pattern()))
			.scriptingLine
			.assertEqualTo("pattern" lineTo script(
				"options" lineTo script("zero", "one")))
	}

	@Test
	fun recursiveAccess() {
		val listPattern = pattern(
			"list" lineTo pattern(
				item("empty"),
				item("link" lineTo pattern(
					item(onceRecurse.increase),
					item("value")))))

		listPattern
			.getOrNull("link")!!
			.assertEqualTo(pattern("link" lineTo listPattern.plus("value" lineTo pattern())))

		listPattern
			.getOrNull("link")!!
			.getOrNull("list")!!
			.assertEqualTo(listPattern)

		listPattern
			.getOrNull("link")!!
			.getOrNull("list")!!
			.getOrNull("link")!!
			.assertEqualTo(pattern("link" lineTo listPattern.plus("value" lineTo pattern())))

		listPattern
			.getOrNull("link")!!
			.getOrNull("list")!!
			.getOrNull("link")!!
			.getOrNull("list")!!
			.assertEqualTo(listPattern)
	}

	@Test
	fun recurseExpand() {
		assertFails { item(onceRecurse).expand() }

		pattern(onceRecurse)
			.expand(root(onceRecurse, "foo".patternLine))
			.assertEqualTo(pattern("foo"))

		pattern()
			.expand()
			.assertEqualTo(pattern())

		pattern("foo")
			.expand()
			.assertEqualTo(pattern("foo"))

		pattern(
			"x" lineTo pattern("zero"),
			"y" lineTo pattern("one"))
			.expand()
			.assertEqualTo(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one")))

		pattern("zero" lineTo pattern(onceRecurse))
			.expand()
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern("zero" lineTo pattern(onceRecurse))))

		pattern("zero" lineTo pattern(onceRecurse))
			.expand()
			.expand()
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern("zero" lineTo pattern(onceRecurse))))

		pattern(
			"zero" lineTo pattern(onceRecurse),
			"one" lineTo pattern())
			.expand()
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern("zero" lineTo pattern(onceRecurse)),
					"one" lineTo pattern()))

		pattern("zero" lineTo pattern("one" lineTo pattern(onceRecurse)))
			.expand()
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(onceRecurse))))

		pattern("zero" lineTo pattern("one" lineTo pattern(onceRecurse.increase)))
			.expand()
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(
							"zero" lineTo pattern(
								"one" lineTo pattern(onceRecurse.increase))))))

		pattern("zero" lineTo pattern("one" lineTo pattern(onceRecurse.increase)))
			.expand()
			.expand()
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(
							"zero" lineTo pattern(
								"one" lineTo pattern(onceRecurse.increase))))))
	}

	@Test
	fun contains() {
		pattern().contains(pattern()).assertEqualTo(true)

		pattern("zero").contains(pattern("zero")).assertEqualTo(true)
		pattern("zero").contains(pattern("one")).assertEqualTo(false)

		pattern(
			"x" lineTo pattern("zero"),
			"y" lineTo pattern("one"))
			.contains(
				pattern(
					"x" lineTo pattern("zero"),
					"y" lineTo pattern("one")))
			.assertEqualTo(true)

		pattern(options("zero", "one")).contains(pattern("zero")).assertEqualTo(true)
		pattern(options("zero", "one")).contains(pattern("one")).assertEqualTo(true)
		pattern(options("zero", "one")).contains(pattern("two")).assertEqualTo(false)

		pattern("bit" lineTo pattern(options("zero", "one")))
			.contains(pattern("bit" lineTo pattern("zero")))
			.assertEqualTo(true)

		pattern(onceRecurse)
			.contains(pattern(onceRecurse))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern(onceRecurse))
			.contains(pattern("zero" lineTo pattern(onceRecurse)))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern(onceRecurse))
			.contains(
				pattern(
					"zero" lineTo pattern(
						"zero" lineTo pattern(onceRecurse))))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern(onceRecurse))
			.contains(
				pattern(
					"zero" lineTo pattern(
						"zero" lineTo pattern(
							"zero" lineTo pattern(onceRecurse)))))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern("one" lineTo pattern(onceRecurse.increase)))
			.contains(pattern("zero" lineTo pattern("one" lineTo pattern(onceRecurse.increase))))
			.assertEqualTo(true)

		pattern("zero" lineTo pattern("one" lineTo pattern(onceRecurse.increase)))
			.contains(
				pattern(
					"zero" lineTo pattern(
						"one" lineTo pattern(
							"zero" lineTo pattern(
								"one" lineTo pattern(
									onceRecurse.increase))))))
			.assertEqualTo(true)
	}

	@Test
	fun previousOrNull() {
		pattern(
			"zero" lineTo pattern(onceRecurse),
			"one" lineTo pattern())
			.previousOrNull
			.assertEqualTo(pattern("zero" lineTo pattern(onceRecurse)))
	}

	@Test
	fun getOrNull() {
		pattern(
			"foo" lineTo pattern(),
			"bit" lineTo pattern(
				"zero" lineTo pattern(onceRecurse.increase)))
			.getOrNull("zero")
			.assertEqualTo(
				pattern(
					"zero" lineTo pattern(
						"bit" lineTo pattern(
							"zero" lineTo pattern(onceRecurse.increase)))))
	}

	@Test
	fun leafNameTraceOrNull() {
		pattern(
			"point" lineTo pattern(
				"x" lineTo pattern("zero"),
				"y" lineTo pattern("one")))
			.leafNameTraceOrNull()
			.assertEqualTo(nameTrace().plus("point").plus("y").plus("one"))
	}
}