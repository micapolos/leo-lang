package leo14.typed.eval

import leo.base.assertEqualTo
import leo14.compile
import leo14.fieldTo
import leo14.lambda.term
import leo14.resultCompiler
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class CompilerTest {
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
	fun deepField() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script("text" fieldTo script("foo" fieldTo "Hello, world!")))
			.assertEqualTo(evalContext.with(term("Hello, world!") of type("text" fieldTo type("foo" fieldTo nativeType))))
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
					term("Hello, world!").typedPlus(term("and everyone"))
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
					term("Hello, world!").typedPlus(term("and everyone")).typedHead
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
					term("Hello, world!").typedPlus(term("and everyone")).typedTail
						of type("text" fieldTo nativeType)))
	}

	@Test
	fun access0() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one",
						"z" fieldTo "two"),
					"x" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					term("zero")
						.typedPlus(term("one"))
						.typedPlus(term("two"))
						.typedTail.typedTail
						of type("x" fieldTo nativeType)))
	}

	@Test
	fun access1() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one",
						"z" fieldTo "two"),
					"y" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					term("zero")
						.typedPlus(term("one"))
						.typedPlus(term("two"))
						.typedTail.typedHead of type("y" fieldTo nativeType)))
	}

	@Test
	fun access2() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one",
						"z" fieldTo "two"),
					"z" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					term("zero")
						.typedPlus(term("one"))
						.typedPlus(term("two"))
						.typedHead of type("z" fieldTo nativeType)))
	}

	@Test
	fun wrap() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one"),
					"z" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					term("zero").typedPlus(term("one")) of
						type("z" fieldTo type(
							"vec" fieldTo type(
								"x" fieldTo nativeType,
								"y" fieldTo nativeType)))))
	}

	@Test
	fun native() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"string" fieldTo "Hello, world!",
					"native" fieldTo script()))
			.assertEqualTo(
				evalContext.with(term("Hello, world!") of nativeType))
	}

	@Test
	fun accessAndNative() {
		compiledCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one"),
					"x" fieldTo script(),
					"native" fieldTo script()))
			.assertEqualTo(
				evalContext.with(term("zero").typedPlus(term("one")).typedTail of nativeType))
	}
}