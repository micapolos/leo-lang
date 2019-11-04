package leo14

import leo13.js.compiler.fallback
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
		val booleanCompiler = switchCompiler(
			leo13.js.compiler.choice("false", resultCompiler(false)),
			leo13.js.compiler.choice("true", resultCompiler(true)))

		booleanCompiler
			.write(token(begin("false")))
			.assertResult(false)

		booleanCompiler
			.write(token(begin("true")))
			.assertResult(true)

		assertFails {
			booleanCompiler
				.write(token(begin("maybe")))
		}
	}

	@Test
	fun choiceWithFallback() {
		val booleanCompiler = switchCompiler(
			fallback(beginCompiler("null") { resultCompiler(null) }),
			leo13.js.compiler.choice("false", resultCompiler(false)),
			leo13.js.compiler.choice("true", resultCompiler(true)))

		booleanCompiler
			.write(token(begin("false")))
			.assertResult(false)

		booleanCompiler
			.write(token(begin("true")))
			.assertResult(true)

		booleanCompiler
			.write(token(begin("null")))
			.assertResult(null)

		assertFails {
			booleanCompiler
				.write(token(begin("maybe")))
		}
	}
}
