package leo13.js

import kotlin.test.Test

class FunctionCompilerTest {
	@Test
	fun test() {
		function(functions()) { ResultCompiler(it) }
			.write(token(begin("given")))
			.write(token(begin("string")))
			.write(token(end))
			.write(token(end))
			.write(token(begin("gives")))
			.write(token(end))
			.write(token(end))
			.assertResult(types(stringType) gives nullTyped)
	}
}