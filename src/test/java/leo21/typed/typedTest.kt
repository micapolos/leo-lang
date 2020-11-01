package leo21.typed

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.doubleType
import leo21.type.fieldTo
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
			"x" fieldTo typed(10.0),
			"y" fieldTo typed(20.0))
			.type
			.assertEqualTo(
				type(
					"x" fieldTo doubleType,
					"y" fieldTo doubleType))
	}

	@Test
	fun struct_duplicateField() {
		assertFails {
			typed(
				"x" fieldTo typed(10.0),
				"x" fieldTo typed(20.0))
		}
	}

	@Test
	fun choice_ok() {
		choiceTyped {
			this
				.plusNotChosen("number" fieldTo doubleType)
				.plusChosen("text" fieldTo typed("foo"))
		}.type
			.assertEqualTo(
				type(
					choice(
						"number" fieldTo doubleType,
						"text" fieldTo stringType)))
	}

	@Test
	fun choice_duplicateField() {
		assertFails {
			choiceTyped {
				this
					.plusNotChosen("number" fieldTo doubleType)
					.plusChosen("number" fieldTo typed("foo"))
			}
		}
	}

	@Test
	fun choice_notChosen() {
		assertFails {
			choiceTyped {
				this
					.plusNotChosen("number" fieldTo doubleType)
					.plusNotChosen("text" fieldTo stringType)
			}
		}
	}

	@Test
	fun choice_chosenTwice() {
		assertFails {
			choiceTyped {
				this
					.plusChosen("number" fieldTo typed(10.0))
					.plusChosen("text" fieldTo typed("foo"))
			}
		}
	}

	@Test
	fun choice_typed() {
		choice(
			"number" fieldTo doubleType,
			"text" fieldTo stringType)
			.typed("number" fieldTo typed(10.0))
			.type
			.assertEqualTo(
				type(
					choice(
						"number" fieldTo doubleType,
						"text" fieldTo stringType)))
	}

	@Test
	fun get_first() {
		typed(
			"point" fieldTo typed(
				"x" fieldTo typed(10.0),
				"y" fieldTo typed(20.0)))
			.get("x")
			.type
			.assertEqualTo(type("x" fieldTo doubleType))
	}

	@Test
	fun get_second() {
		typed(
			"point" fieldTo typed(
				"x" fieldTo typed(10.0),
				"y" fieldTo typed(20.0)))
			.get("y")
			.type
			.assertEqualTo(type("y" fieldTo doubleType))
	}

	@Test
	fun get_missing() {
		assertFails {
			typed(
				"point" fieldTo typed(
					"x" fieldTo typed(10.0),
					"y" fieldTo typed(20.0)))
				.get("z")
		}
	}

	@Test
	fun make() {
		typed(
			"x" fieldTo typed(10.0),
			"y" fieldTo typed(20.0))
			.make("point")
			.type
			.assertEqualTo(
				type(
					"point" fieldTo type(
						"x" fieldTo doubleType,
						"y" fieldTo doubleType)))
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
}
