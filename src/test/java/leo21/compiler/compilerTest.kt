package leo21.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo21.typed.lineTo
import leo21.typed.make
import leo21.typed.typed
import kotlin.test.Test

class CompilerTest {
	@Test
	fun make() {
		Compiler(
			emptyBindings,
			typed(
				"x" lineTo typed(10.0),
				"y" lineTo typed(20.0)))
			.plus("make" lineTo script("point"))
			.assertEqualTo(
				Compiler(
					emptyBindings,
					typed(
						"x" lineTo typed(10.0),
						"y" lineTo typed(20.0))
						.make("point")))
	}
}