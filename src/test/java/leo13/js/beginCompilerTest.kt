package leo13.js

import kotlin.test.Test

class BeginCompilerTest {
	@Test
	fun all() {
		begin("name") {
			end {
				ResultCompiler("OK")
			}
		}
			.write(token(begin("name")))
			.write(token(end))
			.assertResult("OK")
	}
}