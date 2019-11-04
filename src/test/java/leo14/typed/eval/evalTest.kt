package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.compile
import leo14.lambda.term
import leo14.resultCompiler
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun empty() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script())
			.assertEqualTo(evalContext.with(emptyTyped()))

	}

	@Test
	fun string() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script("Hello, world!"))
			.assertEqualTo(evalContext.with(term("Hello, world!") of nativeType))

	}
}