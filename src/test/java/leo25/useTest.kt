package leo25

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo25.natives.path
import java.nio.file.Path
import kotlin.test.Test

class UseTest {
	@Test
	fun scriptUseOrNull() {
		script("lib" lineTo script("open" lineTo script("gl")))
			.useOrNull
			.assertEqualTo(use("lib", "open", "gl"))
	}

	@Test
	fun usePath() {
		use("lib", "open", "gl")
			.path
			.assertEqualTo(Path.of("lib", "open", "gl.leo"))
	}
}