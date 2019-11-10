package leo14.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo14.lambda.*
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
				castTo(type(choice("foo")))
					.assertEqualTo(cast(id<Any>().plus(fn(arg0<Any>().invoke(lastTypedLine.typedField.rhs.term)))))
			}
	}

	@Test
	fun multipleChoice() {
		term("foo").of(type("foo" lineTo nativeType))
			.run {
				castTo(type(choice("foo", "bar")))
					.assertEqualTo(cast(id<Any>().plus(fn(fn(arg1<Any>().invoke(lastTypedLine.typedField.rhs.term))))))
			}

		term("bar").of(type("bar" lineTo nativeType))
			.run {
				castTo(type(choice("foo", "bar")))
					.assertEqualTo(cast(id<Any>().plus(fn(fn(arg0<Any>().invoke(lastTypedLine.typedField.rhs.term))))))
			}
	}

	@Test
	fun deepChoice() {
		term("foo")
			.of(type(nativeLine, "foo" lineTo type()))
			.run {
				castTo(type(nativeLine, line(choice("foo"))))
					.assertEqualTo(cast(previousTyped.term.plus(fn(arg0<Any>().invoke(lastTypedLine.typedField.rhs.term)))))
			}
	}
}