package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.indexed
import leo14.invoke
import leo14.leo
import leo14.number
import java.awt.Point
import kotlin.test.Test

class ScriptizerTest {
	@Test
	fun primitives() {
		emptyScriptizer.run {
			script(leo("text"()), "foo")
				.assertEqualTo(leo("foo"))

			script(leo("number"()), number(10))
				.assertEqualTo(leo(10))

			script(leo("native"()), Point(10, 20))
				.assertEqualTo(leo("native"(Point(10, 20).toString())))

			script(leo("text"), null)
				.assertEqualTo(leo("text"))

			script(leo(10), null)
				.assertEqualTo(leo(10))
		}
	}

	@Test
	fun static() {
		emptyScriptizer.run {
			script(leo("static"("text"())), null)
				.assertEqualTo(leo("text"()))

			script(leo("static"("number"())), null)
				.assertEqualTo(leo("number"()))

			script(leo("static"("native"())), null)
				.assertEqualTo(leo("native"()))
		}
	}

	@Test
	fun either() {
		emptyScriptizer.run {
			script(leo("either"("age"("number"()), "name"("text"()))), 0 indexed "foo")
				.assertEqualTo(leo("name"("foo")))

			script(leo("either"("age"("number"()), "name"("text"()))), 1 indexed number(44))
				.assertEqualTo(leo("age"(44)))

			// TODO: In this case value should be optimized to just index.
			script(leo("either"("true"(), "false"())), 0 indexed null)
				.assertEqualTo(leo("false"()))

			script(leo("either"("true"(), "false"())), 1 indexed null)
				.assertEqualTo(leo("true"()))
		}
	}
}