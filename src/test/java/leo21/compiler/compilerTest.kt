package leo21.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo21.compiled.compiled
import leo21.compiled.lineTo
import leo21.compiled.make
import kotlin.test.Test

class CompilerTest {
	@Test
	fun make() {
		Compiler(
			emptyBindings,
			compiled(
				"x" lineTo compiled(10.0),
				"y" lineTo compiled(20.0)))
			.plus("make" lineTo script("point"))
			.assertEqualTo(
				Compiler(
					emptyBindings,
					compiled(
						"x" lineTo compiled(10.0),
						"y" lineTo compiled(20.0))
						.make("point")))
	}
}