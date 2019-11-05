package leo14.typed

import leo.base.assertEqualTo
import leo13.stack
import leo14.*
import leo14.lambda.first
import leo14.lambda.second
import leo14.lambda.term
import kotlin.test.Test

class TypedCompilerTest {
	val lit: (Literal) -> Any = { it.any }

	@Test
	fun empty() {
		emptyTyped<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script())
			.assertEqualTo(emptyTyped<Any>())
	}

	@Test
	fun literal() {
		emptyTyped<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("foo"))
			.assertEqualTo(emptyTyped<Any>().plusNative(term("foo")))
	}

	@Test
	fun simple() {
		emptyTyped<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("foo" lineTo script()))
			.assertEqualTo(emptyTyped<Any>().plus("foo", emptyTyped()))
	}

	@Test
	fun field() {
		emptyTyped<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("foo" lineTo script("bar")))
			.assertEqualTo(emptyTyped<Any>().plus("foo", term("bar") of nativeType))
	}

	@Test
	fun fields() {
		emptyTyped<Any>()
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script(
				"x" lineTo script("foo"),
				"y" lineTo script("bar")))
			.assertEqualTo(emptyTyped<Any>()
				.plus("x", term("foo") of nativeType)
				.plus("y", term("bar") of nativeType))
	}

	@Test
	fun access0() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("x" lineTo script()))
			.assertEqualTo(term("lhs").first.first of type("x" fieldTo nativeType))
	}

	@Test
	fun access1() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("y" lineTo script()))
			.assertEqualTo(term("lhs").first.second of type("y" fieldTo nativeType))
	}

	@Test
	fun access2() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("z" lineTo script()))
			.assertEqualTo(term("lhs").second of type("z" fieldTo nativeType))
	}

	@Test
	fun wrap() {
		term("lhs")
			.of(type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType)))
			.plusCompiler(stack(), lit) { resultCompiler(it) }
			.compile<Any>(script("v" lineTo script()))
			.assertEqualTo(term("lhs") of type("v" fieldTo type(
				"vec" fieldTo type(
					"x" fieldTo nativeType,
					"y" fieldTo nativeType,
					"z" fieldTo nativeType))))
	}
}