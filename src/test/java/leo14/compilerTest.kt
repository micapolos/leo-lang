package leo14

import leo13.js.compiler.fallback
import kotlin.test.Test
import kotlin.test.assertFails

class CompilerTest {
	@Test
	fun switchToken() {
		val compiler = switchCompiler(
			{ number -> resultCompiler(token(number)) },
			{ string -> resultCompiler(token(string)) },
			{ begin -> resultCompiler(token(begin)) },
			{ end -> resultCompiler(token(end)) })

		compiler.write(token(123)).assertResult(token(123))
		compiler.write(token("foo")).assertResult(token("foo"))
		compiler.write(token(begin("foo"))).assertResult(token(begin("foo")))
		compiler.write(token(end)).assertResult(token(end))
	}

	@Test
	fun result() {
		resultCompiler("ok")
			.assertResult("ok")
	}

	@Test
	fun token() {
		compiler(token(begin("foo"))) { resultCompiler("ok") }
			.write(leo14.token(leo14.begin("foo")))
			.assertResult("ok")
	}

	@Test
	fun begin() {
		beginCompiler("foo") { resultCompiler("ok") }
			.write(leo14.token(leo14.begin("foo")))
			.assertResult("ok")
	}

	@Test
	fun end() {
		endCompiler { resultCompiler("ok") }
			.write(leo14.token(leo14.end))
			.assertResult("ok")
	}

	@Test
	fun choice() {
		val booleanCompiler = switchCompiler(
			leo13.js.compiler.choice("false", resultCompiler(false)),
			leo13.js.compiler.choice("true", resultCompiler(true)))

		booleanCompiler
			.write(leo14.token(leo14.begin("false")))
			.assertResult(false)

		booleanCompiler
			.write(leo14.token(leo14.begin("true")))
			.assertResult(true)

		assertFails {
			booleanCompiler
				.write(leo14.token(leo14.begin("maybe")))
		}
	}

	@Test
	fun choiceWithFallback() {
		val booleanCompiler = switchCompiler(
			fallback(beginCompiler("null") { resultCompiler(null) }),
			leo13.js.compiler.choice("false", resultCompiler(false)),
			leo13.js.compiler.choice("true", resultCompiler(true)))

		booleanCompiler
			.write(leo14.token(leo14.begin("false")))
			.assertResult(false)

		booleanCompiler
			.write(leo14.token(leo14.begin("true")))
			.assertResult(true)

		booleanCompiler
			.write(leo14.token(leo14.begin("null")))
			.assertResult(null)

		assertFails {
			booleanCompiler
				.write(leo14.token(leo14.begin("maybe")))
		}
	}
}
