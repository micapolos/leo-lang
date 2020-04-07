package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.indexed
import leo14.invoke
import leo14.leo
import leo14.number
import java.awt.Point
import kotlin.test.Test
import kotlin.test.assertFails

class TypedScriptTest {
	@Test
	fun primitives() {
		empty.scope
			.script(emptyType.plus(textTypeLine), "foo", null)
			.assertEqualTo(leo("foo"))

		empty.scope
			.script(emptyType.plus(nativeTypeLine), Point(10, 20), null)
			.assertEqualTo(leo("native"(Point(10, 20).toString())))

		empty.scope
			.script(emptyType.plus(numberTypeLine), number(10), null)
			.assertEqualTo(leo(10))
	}

	@Test
	fun enum() {
		val type = emptyType
			.plus(
				emptyEnum
					.plus("true"())
					.plus("false"())
					.line)

		empty.scope
			.script(type, 0, null)
			.assertEqualTo(leo("false"()))

		empty.scope
			.script(type, 1, null)
			.assertEqualTo(leo("true"()))

		assertFails {
			empty.scope.script(type, 2, null)
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

		empty.scope
			.script(type, 0 indexed "foo", null)
			.assertEqualTo(leo("name"("foo")))

		empty.scope
			.script(type, 1 indexed number(123), null)
			.assertEqualTo(leo("age"(123)))

		empty.scope
			.script(type, 2 indexed Point(10, 20), null)
			.assertEqualTo(leo("point"("native"(Point(10, 20).toString()))))

		assertFails {
			empty.scope.script(type, 3 indexed Point(10, 20), null)
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

		empty.scope
			.script(type, number(10) to (number(20) to number(30)), null)
			.assertEqualTo(leo("circle"("radius"(10), "center"("point"("x"(20), "y"(30))))))
	}
}