package leo21.typed

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.stringType
import leo21.type.type
import kotlin.test.Test
import kotlin.test.assertFails

class TypedTest {
	@Test
	fun empty() {
		typed()
			.type
			.assertEqualTo(type())
	}

	@Test
	fun struct() {
		typed(
			"x" lineTo typed(10.0),
			"y" lineTo typed(20.0))
			.type
			.assertEqualTo(
				type(
					"x" lineTo doubleType,
					"y" lineTo doubleType))
	}

	@Test
	fun struct_duplicateField() {
		assertFails {
			typed(
				"x" lineTo typed(10.0),
				"x" lineTo typed(20.0))
		}
	}

	@Test
	fun choice_ok() {
		choiceTyped {
			this
				.plusNotChosen("number" lineTo doubleType)
				.plusChosen("text" lineTo typed("foo"))
		}.type
			.assertEqualTo(
				type(
					choice(
						"number" lineTo doubleType,
						"text" lineTo stringType)))
	}

	@Test
	fun choice_duplicateField() {
		assertFails {
			choiceTyped {
				this
					.plusNotChosen("number" lineTo doubleType)
					.plusChosen("number" lineTo typed("foo"))
			}
		}
	}

	@Test
	fun choice_notChosen() {
		assertFails {
			choiceTyped {
				this
					.plusNotChosen("number" lineTo doubleType)
					.plusNotChosen("text" lineTo stringType)
			}
		}
	}

	@Test
	fun choice_chosenTwice() {
		assertFails {
			choiceTyped {
				this
					.plusChosen("number" lineTo typed(10.0))
					.plusChosen("text" lineTo typed("foo"))
			}
		}
	}

	@Test
	fun choice_typed() {
		choice(
			"number" lineTo doubleType,
			"text" lineTo stringType)
			.typed("number" lineTo typed(10.0))
			.type
			.assertEqualTo(
				type(
					choice(
						"number" lineTo doubleType,
						"text" lineTo stringType)))
	}

	@Test
	fun get_first() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("x")
			.type
			.assertEqualTo(type("x" lineTo doubleType))
	}

	@Test
	fun get_second() {
		typed(
			"point" lineTo typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.get("y")
			.type
			.assertEqualTo(type("y" lineTo doubleType))
	}

	@Test
	fun get_number() {
		typed(
			"x" lineTo typed(10.0))
			.get("number")
			.type
			.assertEqualTo(doubleType)
	}

	@Test
	fun get_text() {
		typed(
			"x" lineTo typed("foo"))
			.get("text")
			.type
			.assertEqualTo(stringType)
	}

	@Test
	fun get_missing() {
		assertFails {
			typed(
				"point" lineTo typed(
					"x" lineTo typed(10.0),
					"y" lineTo typed(20.0)))
				.get("z")
		}
	}

	@Test
	fun make() {
		typed(
			"x" lineTo typed(10.0),
			"y" lineTo typed(20.0))
			.make("point")
			.type
			.assertEqualTo(
				type(
					"point" lineTo type(
						"x" lineTo doubleType,
						"y" lineTo doubleType)))
	}

	@Test
	fun invoke() {
		ArrowTyped(arg(0), doubleType arrowTo stringType)
			.invoke(typed(10.0))
			.type
			.assertEqualTo(stringType)
	}

	@Test
	fun invoke_typeMismatch() {
		assertFails {
			ArrowTyped(arg(0), stringType arrowTo doubleType)
				.invoke(typed(10.0))
		}
	}

	@Test
	fun doublePlus() {
		typed(10.0).doublePlus(typed(20.0)).type.assertEqualTo(doubleType)
		assertFails { typed("foo").doublePlus(typed(20.0)) }
		assertFails { typed(10.0).doublePlus(typed("foo")) }
		assertFails { typed("foo").doublePlus(typed("bar")) }
	}

	@Test
	fun doubleMinus() {
		typed(10.0).doubleMinus(typed(20.0)).type.assertEqualTo(doubleType)
		assertFails { typed("foo").doubleMinus(typed(20.0)) }
		assertFails { typed(10.0).doubleMinus(typed("foo")) }
		assertFails { typed("foo").doubleMinus(typed("bar")) }
	}

	@Test
	fun doubleTimes() {
		typed(10.0).doubleTimes(typed(20.0)).type.assertEqualTo(doubleType)
		assertFails { typed("foo").doubleTimes(typed(20.0)) }
		assertFails { typed(10.0).doubleTimes(typed("foo")) }
		assertFails { typed("foo").doubleTimes(typed("bar")) }
	}

	@Test
	fun stringPlus() {
		typed("foo").stringPlus(typed("bar")).type.assertEqualTo(stringType)
		assertFails { typed("foo").stringPlus(typed(20.0)) }
		assertFails { typed(10.0).stringPlus(typed("foo")) }
		assertFails { typed(10.0).stringPlus(typed(20.0)) }
	}
}
