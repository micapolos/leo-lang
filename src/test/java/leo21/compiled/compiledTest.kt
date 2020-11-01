package leo21.compiled

import leo21.type.doubleLine
import kotlin.test.Test

class CompiledTest {
	@Test
	fun build() {
		compiled()
		compiled(
			"x" lineTo compiled(10),
			"y" lineTo compiled(20))
		compiledChoice {
			this
				.plusNotChosen(doubleLine)
				.plusChosen(compiledLine("foo"))
		}
	}
}
