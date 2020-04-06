package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.empty
import leo.base.indexed
import leo14.invoke
import leo14.leo
import kotlin.test.Test

class TypedScriptTest {
	@Test
	fun choice() {
		val type = empty.type
			.plus(
				empty.choice
					.plus("number".fieldTo(empty.type.plus(native.line.choice)).line)
					.plus("text".fieldTo(empty.type.plus(native.line.choice)).line))

		empty.scope
			.script(type, 0 indexed "foo")
			.assertEqualTo(leo("text"("native"("foo"))))

		empty.scope
			.script(type, 1 indexed 123)
			.assertEqualTo(leo("number"("native"("123"))))
	}
}