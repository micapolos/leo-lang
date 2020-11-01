package leo21.compiled

import leo.base.assertEqualTo
import leo.base.assertNotNull
import leo.base.assertNull
import leo15.numberType
import leo15.textType
import leo21.type.choice
import leo21.type.doubleType
import leo21.type.fieldTo
import leo21.type.stringType
import kotlin.test.Test
import kotlin.test.assertFails

class CompiledTest {
	@Test
	fun empty() {
		compiled()
	}

	@Test
	fun struct() {
		compiled(
			"x" fieldTo compiled(10.0),
			"y" fieldTo compiled(20.0))
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
		}
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
	}

	@Test
	fun get_first() {
		compiled(
			"point" fieldTo compiled(
				"x" fieldTo compiled(10.0),
				"y" fieldTo compiled(20.0)))
			.get("x")
	}

	@Test
	fun get_second() {
		compiled(
			"point" fieldTo compiled(
				"x" fieldTo compiled(10.0),
				"y" fieldTo compiled(20.0)))
			.get("y")
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
}
