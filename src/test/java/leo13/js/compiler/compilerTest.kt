package leo13.js.compiler

import leo13.script.v2.field
import leo13.script.v2.fieldTo
import leo13.script.v2.script
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
			choice(
				"false",
				endCompiler { resultCompiler(false) }),
			choice(
				"true",
				endCompiler { resultCompiler(true) }))

		booleanCompiler
			.write(script(field("false")))
			.assertResult(false)

		booleanCompiler
			.write(script(field("true")))
			.assertResult(true)

		assertFails {
			booleanCompiler
				.write(script("maybe" fieldTo script()))
		}
	}
}
