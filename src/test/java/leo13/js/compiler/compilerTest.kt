package leo13.js.compiler

import kotlin.test.Test
import kotlin.test.assertFails

class CompilerTest {
	@Test
	fun result() {
		resultCompiler("ok")
			.assertResult("ok")
	}

	@Test
	fun token() {
		compiler(token(begin("foo"))) { resultCompiler("ok") }
			.write(token(begin("foo")))
			.assertResult("ok")
	}

	@Test
	fun begin() {
		beginCompiler("foo") { resultCompiler("ok") }
			.write(token(begin("foo")))
			.assertResult("ok")
	}

	@Test
	fun end() {
		endCompiler { resultCompiler("ok") }
			.write(token(end))
			.assertResult("ok")
	}

	@Test
	fun choice() {
		val booleanCompiler = compiler(
			choice(
				"false",
				endCompiler { resultCompiler(false) }),
			choice(
				"true",
				endCompiler { resultCompiler(true) }))

		booleanCompiler
			.write(token(begin("false")))
			.write(token(end))
			.assertResult(false)

		booleanCompiler
			.write(token(begin("true")))
			.write(token(end))
			.assertResult(true)

		assertFails {
			booleanCompiler.write(token(begin("maybe")))
		}
	}
}
