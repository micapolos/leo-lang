package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.lambda.runtime.fn
import leo14.leo
import leo14.literal
import leo14.number
import java.awt.Point
import kotlin.test.Test

class TypedScriptTest {
	@Test
	fun primitives() {
		emptyScope
			.script(emptyType.plus(textTypeLine), "foo", null)
			.assertEqualTo(leo("foo"))

		emptyScope
			.script(emptyType.plus(nativeTypeLine), Point(10, 20), null)
			.assertEqualTo(leo("native"(Point(10, 20).toString())))

		emptyScope
			.script(emptyType.plus(numberTypeLine), number(10), null)
			.assertEqualTo(leo(10))
	}

	@Test
	fun choiceWithStaticAlternatives() {
		val type = emptyType
			.plus("true" lineTo emptyType)
			.or(emptyType.plus("false" lineTo emptyType))

		emptyScope
			.script(type, false, null)
			.assertEqualTo(leo("false"()))

		emptyScope
			.script(type, true, null)
			.assertEqualTo(leo("true"()))
	}

	@Test
	fun choice() {
		val type = emptyType
			.plus(textTypeLine)
			.or(emptyType.plus(numberTypeLine))

		emptyScope
			.script(type, false selectsLhs number(123), null)
			.assertEqualTo(leo(123))

		emptyScope
			.script(type, true selectsLhs "foo", null)
			.assertEqualTo(leo("foo"))
	}

	@Test
	fun structures() {
		val type = emptyType
			.plus("circle" fieldTo emptyType
				.plus("radius" fieldTo emptyType.plus(numberTypeLine))
				.plus("center" fieldTo emptyType
					.plus("point" fieldTo emptyType
						.plus("x" fieldTo emptyType.plus(numberTypeLine))
						.plus("y" fieldTo emptyType.plus(numberTypeLine)))))

		emptyScope
			.script(type, number(10) to (number(20) to number(30)), null)
			.assertEqualTo(leo("circle"("radius"(10), "center"("point"("x"(20), "y"(30))))))
	}

	@Test
	fun recursive() {
		emptyScope
			.script(emptyType.recursive.toType, null, null)
			.assertEqualTo(leo())

		emptyScope
			.script(emptyType.plus(textTypeLine).recursive.toType, "foo", null)
			.assertEqualTo(leo("foo"))

		emptyType
			.plus("zero" lineTo emptyType)
			.or(emptyType.plus("succ" lineTo recurseType))
			.recursive
			.toType
			.let { natType ->
				emptyScope
					.script(natType, true selectsLhs null, null)
					.assertEqualTo(leo("zero"()))

				emptyScope
					.script(natType, false selectsLhs (true selectsLhs null), null)
					.assertEqualTo(leo("succ"("zero"())))

				emptyScope
					.script(natType, false selectsLhs (false selectsLhs (true selectsLhs null)), null)
					.assertEqualTo(leo("succ"("succ"("zero"()))))
			}
	}

	@Test
	fun function() {
		emptyScope.script(
			emptyType.plus(numberTypeLine)
				.functionTo(emptyType.plus(textTypeLine))
				.type,
			fn { it.toString() },
			null)
			.assertEqualTo(leo("function"("number"(), "doing"("text"()))))
	}

	@Test
	fun literals() {
		emptyScope.script(
			emptyType.plus(literal(123).staticTypeLine),
			null,
			null)
			.assertEqualTo(leo(123))

		emptyScope.script(
			emptyType.plus(literal("foo").staticTypeLine),
			null,
			null)
			.assertEqualTo(leo("foo"))
	}
}