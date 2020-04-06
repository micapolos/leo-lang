package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.indexed
import leo14.invoke
import leo14.leo
import leo14.number
import java.awt.Point
import kotlin.test.Test

class TypedScriptTest {
	@Test
	fun choice() {
		val type = emptyType
			.plus(
				emptyChoice
					.plus("point".fieldTo(emptyType.plus(nativeTypeLine.choice)).line)
					.plus("age".fieldTo(emptyType.plus(numberTypeLine.choice)).line)
					.plus("name".fieldTo(emptyType.plus(textTypeLine.choice)).line))

		empty.scope
			.script(type, 0 indexed "foo")
			.assertEqualTo(leo("name"("foo")))

		empty.scope
			.script(type, 1 indexed number(123))
			.assertEqualTo(leo("age"(123)))

		empty.scope
			.script(type, 2 indexed Point(10, 20))
			.assertEqualTo(leo("point"("native"(Point(10, 20).toString()))))
	}
}