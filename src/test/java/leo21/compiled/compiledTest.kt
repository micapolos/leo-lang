package leo21.compiled

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.nativeTerm
import leo14.lambda.term
import leo21.prim.prim
import leo21.type.arrowTo
import leo21.type.choice
import leo21.type.line
import leo21.type.lineTo
import leo21.type.numberType
import leo21.type.recurse
import leo21.type.recursive
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
					"x" lineTo numberType,
					"y" lineTo numberType))
	}

	@Test
	fun choice_ok() {
		choiceTyped {
			this
				.plusNotChosen("number" lineTo numberType)
				.plusChosen("text" lineTo compiled("foo"))
		}.type
			.assertEqualTo(
				type(
					line(
						choice(
							"number" lineTo numberType,
							"text" lineTo stringType))))
	}

	@Test
	fun choice_notChosen() {
		assertFails {
			choiceTyped {
				this
					.plusNotChosen("number" lineTo numberType)
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
			"number" lineTo numberType,
			"text" lineTo stringType)
			.compiled("number" lineTo compiled(10.0))
			.type
			.assertEqualTo(
				type(
					line(
						choice(
							"number" lineTo numberType,
							"text" lineTo stringType))))
	}

	@Test
	fun get_first() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.get("x")
			.type
			.assertEqualTo(type("x" lineTo numberType))
	}

	@Test
	fun get_second() {
		compiled(
			"point" lineTo compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.get("y")
			.type
			.assertEqualTo(type("y" lineTo numberType))
	}

	@Test
	fun get_number() {
		compiled(
			"x" lineTo compiled(10.0))
			.get("number")
			.type
			.assertEqualTo(numberType)
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
						"x" lineTo numberType,
						"y" lineTo numberType)))
	}

	@Test
	fun invoke() {
		ArrowCompiled(arg(0), numberType arrowTo stringType)
			.invoke(compiled(10.0))
			.type
			.assertEqualTo(stringType)
	}

	@Test
	fun invoke_typeMismatch() {
		assertFails {
			ArrowCompiled(arg(0), stringType arrowTo numberType)
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
			.switch(
				"zero" caseTo { compiled("zero") },
				"one" caseTo { compiled("one") }
			)
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
				.plus("one" caseTo { compiled(1) })
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
				.plus("zero" caseTo { compiled(0) })
				.plus("zero" caseTo { compiled(0) })
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
				.plus("zero" caseTo { compiled("foo") })
				.plus("one" caseTo { compiled(123) })
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
				.plus("zero" caseTo { compiled(0) })
				.plus("one" caseTo { compiled(1) })
				.plus("two" caseTo { compiled(2) })
		}
	}

	@Test
	fun recursiveAccessOrNull() {
		nativeTerm(prim("foo"))
			.of(type(line(recursive("foo" lineTo type("bar" lineTo type(line(recurse(0))))))))
			.accessOrNull("foo")
			.assertEqualTo(
				nativeTerm(prim("foo"))
					.of(type(line(recursive("bar" lineTo type("foo" lineTo type(line(recurse(0))))))))
			)
	}

	@Test
	fun arrowOrNull() {
		compiled(line(fn(term(prim("foo"))) of (numberType arrowTo stringType)))
			.arrowOrNull
			.assertEqualTo(fn(term(prim("foo"))) of (numberType arrowTo stringType))
	}
}
