package leo14.untyped.typed

import leo.base.assertEqualTo
import leo14.invoke
import leo14.leo
import leo14.untyped.plusName
import org.junit.Test

class CompilerTest {
	@Test
	fun primitives() {
		emptyCompiler
			.plus(leo(2, plusName(3)))
			.evaluatedScript
			.assertEqualTo(leo(5))
	}
}