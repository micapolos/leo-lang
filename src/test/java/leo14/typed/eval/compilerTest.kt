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
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script())
			.assertEqualTo(evalContext.with(emptyTyped()))

	}

	@Test
	fun string() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script("Hello, world!"))
			.assertEqualTo(evalContext.with(term("Hello, world!") of nativeType))
	}

	@Test
	fun field() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script("text" fieldTo "Hello, world!"))
			.assertEqualTo(evalContext.with(term("Hello, world!") of type("text" fieldTo nativeType)))

	}

	@Test
	fun deepField() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(script("text" fieldTo script("foo" fieldTo "Hello, world!")))
			.assertEqualTo(evalContext.with(term("Hello, world!") of type("text" fieldTo type("foo" fieldTo nativeType))))
	}

	@Test
	fun link() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"text" fieldTo "Hello, world!",
					"hint" fieldTo "and everyone"))
			.assertEqualTo(
				evalContext.with(
					term("Hello, world!").pairTo(term("and everyone"))
						of type("text" fieldTo nativeType, "hint" fieldTo nativeType)))
	}

	@Test
	fun access0() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
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
						.pairTo(term("one"))
						.pairTo(term("two"))
						.typedTail.typedTail
						of type("x" fieldTo nativeType)))
	}

	@Test
	fun access1() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
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
						.pairTo(term("one"))
						.pairTo(term("two"))
						.typedTail.typedHead of type("y" fieldTo nativeType)))
	}

	@Test
	fun access2() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
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
						.pairTo(term("one"))
						.pairTo(term("two"))
						.typedHead of type("z" fieldTo nativeType)))
	}

	@Test
	fun wrap() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one"),
					"z" fieldTo script()))
			.assertEqualTo(
				evalContext.with(
					term("zero").pairTo(term("one")) of
						type("z" fieldTo type(
							"vec" fieldTo type(
								"x" fieldTo nativeType,
								"y" fieldTo nativeType)))))
	}

	@Test
	fun native() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"string" fieldTo "Hello, world!",
					"native" fieldTo script()))
			.assertEqualTo(
				evalContext.with(term("Hello, world!") of nativeType))
	}

	@Test
	fun accessAndNative() {
		termCompiler(evalContext.with(emptyTyped())) { resultCompiler(it) }
			.compile<Any>(
				script(
					"vec" fieldTo script(
						"x" fieldTo "zero",
						"y" fieldTo "one"),
					"x" fieldTo script(),
					"native" fieldTo script()))
			.assertEqualTo(
				evalContext.with(term("zero").pairTo(term("one")).typedTail of nativeType))
	}
}