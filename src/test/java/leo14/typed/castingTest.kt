package leo14.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo13.index0
import leo13.index1
import leo13.index2
import leo14.lambda.choiceTerm
import leo14.lambda.id
import leo14.lambda.term
import kotlin.test.Test

class CastingTest {
	@Test
	fun empty() {
		term("foo").of(type())
			.castTo(type())
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun field_match() {
		term("foo").of(type("field" lineTo type()))
			.castTo(type("field" lineTo type()))
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun field_mismatch() {
		term("foo").of(type("field" lineTo type()))
			.castTo(type("field2" lineTo type()))
			.assertEqualTo(null)
	}

	@Test
	fun fields_match() {
		term("foo")
			.of(type("x" lineTo type(), "y" lineTo type()))
			.castTo(type("x" lineTo type(), "y" lineTo type()))
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun fields_mismatch() {
		term("foo")
			.of(type("x" lineTo type(), "y" lineTo type()))
			.castTo(type("x" lineTo type(), "z" lineTo type()))
			.assertEqualTo(null)
	}

	@Test
	fun fields_suffix() {
		id<Any>()
			.of(type("x" lineTo type(), "y" lineTo type()))
			.castTo(type("y" lineTo type()))
			.assertEqualTo(null)
	}

	@Test
	fun struct() {
		term("foo")
			.of(
				type(
					"vec" lineTo type(
						"x" lineTo numberType,
						"y" lineTo type())))
			.castTo(
				type(
					"vec" lineTo type(
						"x" lineTo numberType,
						"y" lineTo type())))
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun native() {
		term("lhs")
			.of(numberType)
			.castTo(numberType)
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun singleChoice() {
		term("foo")
			.of(type("foo" lineTo numberType))
			.run {
				castTermTo(type(choice("foo" optionTo numberType)))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index0, index1, lastTypedLine.typedField.rhs.term)
									.of("foo" lineTo numberType)).term)
			}
	}

	@Test
	fun multipleChoice() {
		term("foo").of(type("foo" lineTo numberType))
			.run {
				castTermTo(type(choice("foo" optionTo numberType, "bar" optionTo numberType)))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index1, index2, lastTypedLine.typedField.rhs.term)
									.of("foo" lineTo numberType)).term)
			}

		term("bar").of(type("bar" lineTo numberType))
			.run {
				castTermTo(type(choice("foo" optionTo numberType, "bar" optionTo numberType)))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index0, index2, lastTypedLine.typedField.rhs.term)
									.of(line(choice("foo", "bar")))).term)
			}
	}

	@Test
	fun deepChoice_native() {
		term("foo")
			.of(type(numberLine, "foo" lineTo type()))
			.run {
				castTermTo(type(numberLine, line(choice("foo"))))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index0, index1, lastTypedLine.typedField.rhs.term)
									.of(line(choice("foo"))))
							.term)
			}
	}

	@Test
	fun deepChoice_simple() {
		term("foo")
			.of(type("goo" lineTo type(), "foo" lineTo type()))
			.run {
				castTermTo(type("goo" lineTo type(), line(choice("foo"))))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index0, index1, lastTypedLine.typedField.rhs.term)
									.of(line(choice("foo"))))
							.term)
			}
	}
}