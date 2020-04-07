package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.indexed
import leo14.invoke
import leo14.lambda.runtime.fn
import leo14.leo
import leo14.literal
import leo14.number
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertFails

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
			.plus(
				emptyChoice
					.plus("true" lineTo emptyType)
					.plus("false" lineTo emptyType)
					.line)

		emptyScope
			.script(type, 0, null)
			.assertEqualTo(leo("false"()))

		emptyScope
			.script(type, 1, null)
			.assertEqualTo(leo("true"()))

		assertFails {
			emptyScope.script(type, 2, null)
		}
	}

	@Test
	fun choice() {
		val type = emptyType
			.plus(
				emptyChoice
					.plus("point".fieldTo(emptyType.plus(nativeTypeLine)).line)
					.plus("age".fieldTo(emptyType.plus(numberTypeLine)).line)
					.plus("name".fieldTo(emptyType.plus(textTypeLine)).line)
					.line)

		emptyScope
			.script(type, 0 indexed "foo", null)
			.assertEqualTo(leo("name"("foo")))

		emptyScope
			.script(type, 1 indexed number(123), null)
			.assertEqualTo(leo("age"(123)))

		emptyScope
			.script(type, 2 indexed Point(10, 20), null)
			.assertEqualTo(leo("point"("native"(Point(10, 20).toString()))))

		assertFails {
			emptyScope.script(type, 3 indexed Point(10, 20), null)
		}
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
			.plus(emptyChoice
				.plus("zero" lineTo emptyType)
				.plus("succ" lineTo recurseType)
				.line)
			.recursive
			.toType
			.let { natType ->
				emptyScope
					.script(natType, 1 indexed null, null)
					.assertEqualTo(leo("zero"()))

				emptyScope
					.script(natType, 0 indexed (1 indexed null), null)
					.assertEqualTo(leo("succ"("zero"())))

				emptyScope
					.script(natType, 0 indexed (0 indexed (1 indexed null)), null)
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