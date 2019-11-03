package leo14.lambda

import leo14.*
import kotlin.test.Test

class CompilerTest {
	val compileFallback: Compile<Value<String>> = { errorCompiler("") }
	val compileString: Compile<String> = { ret -> stringCompiler(ret) }

	@Test
	fun native() {
		valueCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("native" fieldTo "ok"))
			.write(token(end))
			.assertResult(value("ok"))
	}

	@Test
	fun argument() {
		valueCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script(field("argument")))
			.write(token(end))
			.assertResult(arg0<String>())
	}

	@Test
	fun argument_previous() {
		valueCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("argument" fieldTo script("previous" fieldTo script())))
			.write(token(end))
			.assertResult(arg1<String>())
	}

	@Test
	fun function() {
		valueCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("function" fieldTo script("native" fieldTo "body")))
			.write(token(end))
			.assertResult(fn(value("body")))
	}

	@Test
	fun apply() {
		valueCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(
				script(
					"native" fieldTo "lhs",
					"apply" fieldTo script("native" fieldTo "rhs")))
			.write(token(end))
			.assertResult(value("lhs")(value("rhs")))
	}

	@Test
	fun valuePlusCompiler_empty() {
		value("lhs")
			.plusCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(token(end))
			.assertResult(value("lhs"))
	}

	@Test
	fun valuePlusCompiler_nonEmpty() {
		value("lhs")
			.plusCompiler(compileFallback, compileString) { resultCompiler(it) }
			.write(script("apply" fieldTo script("native" fieldTo "rhs")))
			.write(token(end))
			.assertResult(value("lhs")(value("rhs")))
	}

	@Test
	fun fallback() {
		valueCompiler({ beginCompiler("jajeczko") { resultCompiler(value("fallback")) } }, compileString) { resultCompiler(it) }
			.write(token(begin("jajeczko")))
			.assertResult(value("fallback"))
	}
}