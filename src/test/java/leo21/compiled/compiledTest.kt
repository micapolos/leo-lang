package leo21.compiled

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo21.typed.lineTo
import leo21.typed.make
import leo21.typed.typed
import kotlin.test.Test

class CompiledTest {
	@Test
	fun make() {
		Compiled(
			emptyBindings,
			typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.plus("make" lineTo script("point"))
			.assertEqualTo(
				Compiled(
					emptyBindings,
					typed(
						"x" lineTo typed(10.0),
						"y" lineTo typed(20.0))
						.make("point")))
	}
}