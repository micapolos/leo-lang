package leo21.compiled

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
			"x" fieldTo compiled(10.0),
			"y" fieldTo compiled(20.0))
			.type
			.assertEqualTo(
				type(
					"x" fieldTo doubleType,
					"y" fieldTo doubleType))
	}

	@Test
	fun struct_duplicateField() {
		assertFails {
			compiled(
				"x" fieldTo compiled(10.0),
				"x" fieldTo compiled(20.0))
		}
	}

	@Test
	fun choice_ok() {
		compiledChoice {
			this
				.plusNotChosen("number" fieldTo doubleType)
				.plusChosen("text" fieldTo compiled("foo"))
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
			compiledChoice {
				this
					.plusNotChosen("number" fieldTo doubleType)
					.plusChosen("number" fieldTo compiled("foo"))
			}
		}
	}

	@Test
	fun choice_notChosen() {
		assertFails {
			compiledChoice {
				this
					.plusNotChosen("number" fieldTo doubleType)
					.plusNotChosen("text" fieldTo stringType)
			}
		}
	}

	@Test
	fun choice_chosenTwice() {
		assertFails {
			compiledChoice {
				this
					.plusChosen("number" fieldTo compiled(10.0))
					.plusChosen("text" fieldTo compiled("foo"))
			}
		}
	}

	@Test
	fun choice_compiled() {
		choice(
			"number" fieldTo doubleType,
			"text" fieldTo stringType)
			.compiled("number" fieldTo compiled(10.0))
			.type
			.assertEqualTo(
				type(
					choice(
						"number" fieldTo doubleType,
						"text" fieldTo stringType)))
	}

	@Test
	fun get_first() {
		compiled(
			"point" fieldTo compiled(
				"x" fieldTo compiled(10.0),
				"y" fieldTo compiled(20.0)))
			.get("x")
			.type
			.assertEqualTo(type("x" fieldTo doubleType))
	}

	@Test
	fun get_second() {
		compiled(
			"point" fieldTo compiled(
				"x" fieldTo compiled(10.0),
				"y" fieldTo compiled(20.0)))
			.get("y")
			.type
			.assertEqualTo(type("y" fieldTo doubleType))
	}

	@Test
	fun get_missing() {
		assertFails {
			compiled(
				"point" fieldTo compiled(
					"x" fieldTo compiled(10.0),
					"y" fieldTo compiled(20.0)))
				.get("z")
		}
	}

	@Test
	fun make() {
		compiled(
			"x" fieldTo compiled(10.0),
			"y" fieldTo compiled(20.0))
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
}
