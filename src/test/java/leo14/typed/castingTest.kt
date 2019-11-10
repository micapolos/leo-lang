package leo14.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo13.index0
import leo13.index1
import leo13.index2
import leo14.lambda.choiceTerm
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
	fun field() {
		term("foo").of(type("field" lineTo type()))
			.castTo(type())
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun fields() {
		term("foo").of(type(
			"x" lineTo type(),
			"y" lineTo type()))
			.castTo(type())
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun struct() {
		term("foo")
			.of(
				type(
					"vec" lineTo type(
						"x" lineTo nativeType,
						"y" lineTo type())))
			.castTo(
				type(
					"vec" lineTo type(
						"x" lineTo nativeType,
						"y" lineTo type())))
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun native() {
		term("lhs")
			.of(nativeType)
			.castTo(nativeType)
			.assertEqualTo(cast<Any>(empty))
	}

	@Test
	fun singleChoice() {
		term("foo")
			.of(type("foo" lineTo nativeType))
			.run {
				castTermTo(type(choice("foo")))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index0, index1, lastTypedLine.typedField.rhs.term)
									.of("foo" lineTo nativeType)).term)
			}
	}

	@Test
	fun multipleChoice() {
		term("foo").of(type("foo" lineTo nativeType))
			.run {
				castTermTo(type(choice("foo", "bar")))
					.assertEqualTo(
						previousTyped
							.plus(
								choiceTerm(index1, index2, lastTypedLine.typedField.rhs.term)
									.of("foo" lineTo nativeType)).term)
			}

		term("bar").of(type("bar" lineTo nativeType))
			.run {
				castTermTo(type(choice("foo", "bar")))
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
			.of(type(nativeLine, "foo" lineTo type()))
			.run {
				castTermTo(type(nativeLine, line(choice("foo"))))
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