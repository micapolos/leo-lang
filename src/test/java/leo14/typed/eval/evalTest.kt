package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.compile
import leo14.fieldTo
import leo14.lambda.first
import leo14.lambda.pair
import leo14.lambda.second
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

	@Test
	fun field() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script("text" fieldTo "Hello, world!"))
			.assertEqualTo(evalContext.with(term("Hello, world!") of type("text" fieldTo nativeType)))

	}

	@Test
	fun link() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"text" fieldTo "Hello, world!",
					"hint" fieldTo "and everyone"))
			.assertEqualTo(
				evalContext.with(
					pair(term("Hello, world!"), term("and everyone"))
						of type("text" fieldTo nativeType, "hint" fieldTo nativeType)))
	}

	@Test
	fun head() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"text" fieldTo "Hello, world!",
					"hint" fieldTo "and everyone",
					"head" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					pair(term("Hello, world!"), term("and everyone")).second
						of nativeType))
	}

	@Test
	fun tail() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"text" fieldTo "Hello, world!",
					"hint" fieldTo "and everyone",
					"tail" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					pair(term("Hello, world!"), term("and everyone")).first
						of type("text" fieldTo nativeType)))
	}
}