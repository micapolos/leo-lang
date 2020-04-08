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
			.script(emptyType.plus(textTypeLine).typed("foo"))
			.assertEqualTo(leo("foo"))

		emptyScope
			.script(emptyType.plus(nativeTypeLine).typed(Point(10, 20)))
			.assertEqualTo(leo("native"(Point(10, 20).toString())))

		emptyScope
			.script(emptyType.plus(numberTypeLine).typed(number(10)))
			.assertEqualTo(leo(10))
	}

	@Test
	fun choiceWithStaticAlternatives() {
		val type = emptyType
			.plus("false" lineTo emptyType)
			.or(emptyType.plus("true" lineTo emptyType))

		emptyScope
			.script(type.typed(true))
			.assertEqualTo(leo("true"()))

		emptyScope
			.script(type.typed(false))
			.assertEqualTo(leo("false"()))
	}

	@Test
	fun choice() {
		val type = emptyType
			.plus(textTypeLine)
			.or(emptyType.plus(numberTypeLine))

		emptyScope
			.script(type.typed(true rhsSelected number(123)))
			.assertEqualTo(leo(123))

		emptyScope
			.script(type.typed(false rhsSelected "foo"))
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
			.script(type.typed(number(10) to (number(20) to number(30))))
			.assertEqualTo(leo("circle"("radius"(10), "center"("point"("x"(20), "y"(30))))))
	}

	@Test
	fun recursive() {
		emptyScope
			.script(emptyType.recursive.toType.staticTyped)
			.assertEqualTo(leo())

		emptyScope
			.script(emptyType.plus(textTypeLine).recursive.toType.typed("foo"))
			.assertEqualTo(leo("foo"))

		emptyType
			.plus("zero" lineTo emptyType)
			.or(emptyType.plus("next" lineTo recurseType))
			.recursive
			.toType
			.let { natType ->
				emptyScope
					.script(natType.typed(false rhsSelected null))
					.assertEqualTo(leo("zero"()))

				emptyScope
					.script(natType.typed(true rhsSelected (false rhsSelected null)))
					.assertEqualTo(leo("next"("zero"())))

				emptyScope
					.script(natType.typed(true rhsSelected (true rhsSelected (false rhsSelected null))))
					.assertEqualTo(leo("next"("next"("zero"()))))
			}
	}

	@Test
	fun function() {
		emptyScope.script(
			emptyType.plus(numberTypeLine)
				.functionTo(emptyType.plus(textTypeLine))
				.type
				.typed(fn { it.toString() }))
			.assertEqualTo(leo("function"("number"(), "doing"("text"()))))
	}

	@Test
	fun literals() {
		emptyScope.script(
			emptyType.plus(literal(123).staticTypeLine).staticTyped)
			.assertEqualTo(leo(123))

		emptyScope.script(
			emptyType.plus(literal("foo").staticTypeLine).staticTyped)
			.assertEqualTo(leo("foo"))
	}

	@Test
	fun anything() {
		emptyScope
			.script(anythingType.typed(textType.typed("foo")))
			.assertEqualTo(leo("foo"))
	}
}