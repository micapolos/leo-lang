package leo13.lambda

import leo13.js.compiler.*
import kotlin.test.Test

class CompilerTest {
	val compileString: Compile<String> = { ret -> stringCompiler(ret) }

	@Test
	fun native() {
		valueCompiler(compileString) { resultCompiler(it) }
			.write(script("native" fieldTo "ok"))
			.write(token(end))
			.assertResult(value("ok"))
	}

	@Test
	fun variable0() {
		valueCompiler(compileString) { resultCompiler(it) }
			.write(script(field("variable")))
			.write(token(end))
			.assertResult(arg0<String>())
	}

	@Test
	fun variable1() {
		valueCompiler(compileString) { resultCompiler(it) }
			.write(script("variable" fieldTo script("previous" fieldTo script())))
			.write(token(end))
			.assertResult(arg1<String>())
	}

	@Test
	fun function() {
		valueCompiler(compileString) { resultCompiler(it) }
			.write(script("function" fieldTo script("native" fieldTo "body")))
			.write(token(end))
			.assertResult(fn(value("body")))
	}

	@Test
	fun apply() {
		valueCompiler(compileString) { resultCompiler(it) }
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
			.plusCompiler(compileString) { resultCompiler(it) }
			.write(token(end))
			.assertResult(value("lhs"))
	}

	@Test
	fun valuePlusCompiler_nonEmpty() {
		value("lhs")
			.plusCompiler(compileString) { resultCompiler(it) }
			.write(script("apply" fieldTo script("native" fieldTo "rhs")))
			.write(token(end))
			.assertResult(value("lhs")(value("rhs")))
	}
}