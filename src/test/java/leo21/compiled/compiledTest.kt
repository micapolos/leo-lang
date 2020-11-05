package leo21.compiled

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

class CompiledTest {
	@Test
	fun empty() {
		compiled()
			.type
			.assertEqualTo(type())
	}

	@Test
	fun struct() {
		compiled(
			"x" lineTo compiled(10.0),
			"y" lineTo compiled(20.0))
			.type
			.assertEqualTo(
				type(
					"x" lineTo doubleType,
					"y" lineTo doubleType))
	}

	@Test
	fun struct_duplicateField() {
		assertFails {
			compiled(
				"x" lineTo compiled(10.0),
				"x" lineTo compiled(20.0))
		}
	}

	@Test
	fun choice_ok() {
		choiceTyped {
			this
				.plusNotChosen("number" lineTo doubleType)
				.plusChosen("text" lineTo compiled("foo"))
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
					.plusChosen("number" lineTo compiled("foo"))
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
					.plusChosen("number" lineTo compiled(10.0))
					.plusChosen("text" lineTo compiled("foo"))
			}
		}
	}

	@Test
	fun choice_typed() {
		choice(
			"number" lineTo doubleType,
			"text" lineTo stringType)
			.compiled("number" lineTo compiled(10.0))
			.type
			.assertEqualTo(
				type(
					choice(
						"number" lineTo doubleType,
						"text" lineTo stringType)))
	}

	@Test
	fun get_first() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.get("x")
			.type
			.assertEqualTo(type("x" lineTo doubleType))
	}

	@Test
	fun get_second() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.get("y")
			.type
			.assertEqualTo(type("y" lineTo doubleType))
	}

	@Test
	fun get_number() {
		compiled(
			"x" lineTo compiled(10.0))
			.get("number")
			.type
			.assertEqualTo(doubleType)
	}

	@Test
	fun get_text() {
		compiled(
			"x" lineTo compiled("foo"))
			.get("text")
			.type
			.assertEqualTo(stringType)
	}

	@Test
	fun get_missing() {
		assertFails {
			compiled(
				"point" lineTo compiled(
					"x" lineTo compiled(10.0),
					"y" lineTo compiled(20.0)))
				.get("z")
		}
	}

	@Test
	fun make() {
		compiled(
			"x" lineTo compiled(10.0),
			"y" lineTo compiled(20.0))
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
		ArrowCompiled(arg(0), doubleType arrowTo stringType)
			.invoke(compiled(10.0))
			.type
			.assertEqualTo(stringType)
	}

	@Test
	fun invoke_typeMismatch() {
		assertFails {
			ArrowCompiled(arg(0), stringType arrowTo doubleType)
				.invoke(compiled(10.0))
		}
	}

	@Test
	fun switch() {
		compiled(
			"bit" lineTo choice(
				"zero" lineTo type(),
				"one" lineTo type())
				.compiled("zero" lineTo compiled()))
			.switch
			.case("zero", ArrowCompiled(arg(0), type("zero" lineTo type()) arrowTo stringType))
			.case("one", ArrowCompiled(arg(0), type("one" lineTo type()) arrowTo stringType))
			.end
			.type
			.assertEqualTo(stringType)
	}

	@Test
	fun switch_notChoice() {
		assertFails { compiled("bit" lineTo compiled("zero")).switch }
	}

	@Test
	fun switch_firstCaseMismatch() {
		assertFails {
			compiled(
				"bit" lineTo choice(
					"zero" lineTo type(),
					"one" lineTo type())
					.compiled("zero" lineTo compiled()))
				.switch
				.case("one", ArrowCompiled(arg(0), type("one" lineTo type()) arrowTo stringType))
		}
	}

	@Test
	fun switch_secondCaseMismatch() {
		assertFails {
			compiled(
				"bit" lineTo choice(
					"zero" lineTo type(),
					"one" lineTo type())
					.compiled("zero" lineTo compiled()))
				.switch
				.case("zero", ArrowCompiled(arg(0), type("zero" lineTo type()) arrowTo stringType))
				.case("zero", ArrowCompiled(arg(0), type("zero" lineTo type()) arrowTo stringType))
		}
	}

	@Test
	fun switch_arrowLhsTypeMismatch() {
		assertFails {
			compiled(
				"bit" lineTo choice(
					"zero" lineTo type(),
					"one" lineTo type())
					.compiled("zero" lineTo compiled()))
				.switch
				.case("zero", ArrowCompiled(arg(0), type("foo" lineTo type()) arrowTo stringType))
		}
	}

	@Test
	fun switch_arrowRhsTypeMismatch() {
		assertFails {
			compiled(
				"bit" lineTo choice(
					"zero" lineTo type(),
					"one" lineTo type())
					.compiled("zero" lineTo compiled()))
				.switch
				.case("zero", ArrowCompiled(arg(0), type("zero" lineTo type()) arrowTo stringType))
				.case("one", ArrowCompiled(arg(0), type("one" lineTo type()) arrowTo doubleType))
		}
	}

	@Test
	fun switch_notExhaustive() {
		assertFails {
			compiled(
				"bit" lineTo choice(
					"zero" lineTo type(),
					"one" lineTo type())
					.compiled("zero" lineTo compiled()))
				.switch
				.end
		}
	}

	@Test
	fun switch_exhausted() {
		assertFails {
			compiled(
				"bit" lineTo choice(
					"zero" lineTo type(),
					"one" lineTo type())
					.compiled("zero" lineTo compiled()))
				.switch
				.case("zero", ArrowCompiled(arg(0), type("zero" lineTo type()) arrowTo stringType))
				.case("one", ArrowCompiled(arg(0), type("one" lineTo type()) arrowTo stringType))
				.case("two", ArrowCompiled(arg(0), type("two" lineTo type()) arrowTo stringType))
		}
	}
}
