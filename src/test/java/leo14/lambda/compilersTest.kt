package leo14.lambda

import leo14.*
import kotlin.test.Test

class CompilerTest {
	val compileFallback: Compile<Term<String>> = { errorCompiler("") }
	val compileString: Compile<String> = { ret -> stringCompiler(ret) }

	@Test
	fun native() {
		termCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("native" fieldTo literal("ok")))
			.write(token(end))
			.assertResult(term("ok"))
	}

	@Test
	fun argument() {
		termCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script(field("argument")))
			.write(token(end))
			.assertResult(arg0<String>())
	}

	@Test
	fun argument_previous() {
		termCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("argument" fieldTo script("previous" fieldTo script())))
			.write(token(end))
			.assertResult(arg1<String>())
	}

	@Test
	fun function() {
		termCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("function" fieldTo script("native" fieldTo literal("body"))))
			.write(token(end))
			.assertResult(fn(term("body")))
	}

	@Test
	fun apply() {
		termCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(
				script(
					"native" fieldTo literal("lhs"),
					"apply" fieldTo script("native" fieldTo literal("rhs"))))
			.write(token(end))
			.assertResult(term("lhs")(term("rhs")))
	}

	@Test
	fun valuePlusCompiler_empty() {
		term("lhs")
			.plusCompiler(compileTerm(compileFallback, compileString)) { resultCompiler(it) }
			.write(token(end))
			.assertResult(term("lhs"))
	}

	@Test
	fun valuePlusCompiler_nonEmpty() {
		term("lhs")
			.plusCompiler(compileTerm(compileFallback, compileString)) { resultCompiler(it) }
			.write(script("apply" fieldTo script("native" fieldTo literal("rhs"))))
			.write(token(end))
			.assertResult(term("lhs")(term("rhs")))
	}

	@Test
	fun fallback() {
		termCompiler({ beginCompiler("jajeczko") { resultCompiler(term("fallback")) } }, compileString) { resultCompiler(it) }
			.write(token(begin("jajeczko")))
			.assertResult(term("fallback"))
	}
}