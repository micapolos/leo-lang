package leo13.type

import leo.base.assertEqualTo
import leo13.compiler.nameTrace
import leo13.script.lineTo
import leo13.script.script
import leo13.typeName
import kotlin.test.Test
import kotlin.test.assertFails

class TypeTest {
	@Test
	fun scriptLine() {
		type()
			.scriptingLine
			.assertEqualTo(typeName lineTo script())

		type("zero")
			.scriptingLine
			.assertEqualTo(typeName lineTo script("zero"))

		type(
			"zero" lineTo type(),
			"plus" lineTo type("one"))
			.scriptingLine
			.assertEqualTo(typeName lineTo script(
				"zero" lineTo script(),
				"plus" lineTo script("one")))

		type(
			options(
				"zero" lineTo type(),
				"one" lineTo type()))
			.scriptingLine
			.assertEqualTo(typeName lineTo script(
				"options" lineTo script("zero", "one")))
	}

	@Test
	fun recursiveAccess() {
		val listType = type(
			"list" lineTo type(
				item("empty"),
				item("link" lineTo type(
					item(onceRecurse.increase),
					item("value")))))

		listType
			.getOrNull("link")!!
			.assertEqualTo(type("link" lineTo listType.plus("value" lineTo type())))

		listType
			.getOrNull("link")!!
			.getOrNull("list")!!
			.assertEqualTo(listType)

		listType
			.getOrNull("link")!!
			.getOrNull("list")!!
			.getOrNull("link")!!
			.assertEqualTo(type("link" lineTo listType.plus("value" lineTo type())))

		listType
			.getOrNull("link")!!
			.getOrNull("list")!!
			.getOrNull("link")!!
			.getOrNull("list")!!
			.assertEqualTo(listType)
	}

	@Test
	fun recurseExpand() {
		assertFails { item(onceRecurse).expand() }

		type(onceRecurse)
			.expand(root(onceRecurse, "foo".typeLine))
			.assertEqualTo(type("foo"))

		type()
			.expand()
			.assertEqualTo(type())

		type("foo")
			.expand()
			.assertEqualTo(type("foo"))

		type(
			"x" lineTo type("zero"),
			"y" lineTo type("one"))
			.expand()
			.assertEqualTo(
				type(
					"x" lineTo type("zero"),
					"y" lineTo type("one")))

		type("zero" lineTo type(onceRecurse))
			.expand()
			.assertEqualTo(
				type(
					"zero" lineTo type("zero" lineTo type(onceRecurse))))

		type("zero" lineTo type(onceRecurse))
			.expand()
			.expand()
			.assertEqualTo(
				type(
					"zero" lineTo type("zero" lineTo type(onceRecurse))))

		type(
			"zero" lineTo type(onceRecurse),
			"one" lineTo type())
			.expand()
			.assertEqualTo(
				type(
					"zero" lineTo type("zero" lineTo type(onceRecurse)),
					"one" lineTo type()))

		type("zero" lineTo type("one" lineTo type(onceRecurse)))
			.expand()
			.assertEqualTo(
				type(
					"zero" lineTo type(
						"one" lineTo type(onceRecurse))))

		type("zero" lineTo type("one" lineTo type(onceRecurse.increase)))
			.expand()
			.assertEqualTo(
				type(
					"zero" lineTo type(
						"one" lineTo type(
							"zero" lineTo type(
								"one" lineTo type(onceRecurse.increase))))))

		type("zero" lineTo type("one" lineTo type(onceRecurse.increase)))
			.expand()
			.expand()
			.assertEqualTo(
				type(
					"zero" lineTo type(
						"one" lineTo type(
							"zero" lineTo type(
								"one" lineTo type(onceRecurse.increase))))))
	}

	@Test
	fun contains() {
		type().contains(type()).assertEqualTo(true)

		type("zero").contains(type("zero")).assertEqualTo(true)
		type("zero").contains(type("one")).assertEqualTo(false)

		type(
			"x" lineTo type("zero"),
			"y" lineTo type("one"))
			.contains(
				type(
					"x" lineTo type("zero"),
					"y" lineTo type("one")))
			.assertEqualTo(true)

		type(options("zero", "one")).contains(type("zero")).assertEqualTo(true)
		type(options("zero", "one")).contains(type("one")).assertEqualTo(true)
		type(options("zero", "one")).contains(type("two")).assertEqualTo(false)

		type("bit" lineTo type(options("zero", "one")))
			.contains(type("bit" lineTo type("zero")))
			.assertEqualTo(true)

		type(onceRecurse)
			.contains(type(onceRecurse))
			.assertEqualTo(true)

		type("zero" lineTo type(onceRecurse))
			.contains(type("zero" lineTo type(onceRecurse)))
			.assertEqualTo(true)

		type("zero" lineTo type(onceRecurse))
			.contains(
				type(
					"zero" lineTo type(
						"zero" lineTo type(onceRecurse))))
			.assertEqualTo(true)

		type("zero" lineTo type(onceRecurse))
			.contains(
				type(
					"zero" lineTo type(
						"zero" lineTo type(
							"zero" lineTo type(onceRecurse)))))
			.assertEqualTo(true)

		type("zero" lineTo type("one" lineTo type(onceRecurse.increase)))
			.contains(type("zero" lineTo type("one" lineTo type(onceRecurse.increase))))
			.assertEqualTo(true)

		type("zero" lineTo type("one" lineTo type(onceRecurse.increase)))
			.contains(
				type(
					"zero" lineTo type(
						"one" lineTo type(
							"zero" lineTo type(
								"one" lineTo type(
									onceRecurse.increase))))))
			.assertEqualTo(true)
	}

	@Test
	fun previousOrNull() {
		type(
			"zero" lineTo type(onceRecurse),
			"one" lineTo type())
			.previousOrNull
			.assertEqualTo(type("zero" lineTo type(onceRecurse)))
	}

	@Test
	fun getOrNull() {
		type(
			"foo" lineTo type(),
			"bit" lineTo type(
				"zero" lineTo type(onceRecurse.increase)))
			.getOrNull("zero")
			.assertEqualTo(
				type(
					"zero" lineTo type(
						"bit" lineTo type(
							"zero" lineTo type(onceRecurse.increase)))))
	}

	@Test
	fun leafNameTraceOrNull() {
		type(
			"point" lineTo type(
				"x" lineTo type("zero"),
				"y" lineTo type("one")))
			.leafNameTraceOrNull()
			.assertEqualTo(nameTrace().plus("point").plus("y").plus("one"))
	}

	@Test
	fun isStatic() {
		type().isStatic.assertEqualTo(true)
		type("x" lineTo type("zero"), "y" lineTo type("one")).isStatic.assertEqualTo(true)
		type(options()).isStatic.assertEqualTo(false)
		type(options("zero")).isStatic.assertEqualTo(false)
		type(options("zero", "one")).isStatic.assertEqualTo(false)
		type(type(options()) arrowTo type()).isStatic.assertEqualTo(true)
		type(type(options()) arrowTo type(options())).isStatic.assertEqualTo(false)
		type(item(onceRecurse)).isStatic.assertEqualTo(false)
	}
}