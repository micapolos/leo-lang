package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.ifOrNull
import leo14.lambda.runtime.Fn
import leo14.lambda.runtime.fn
import leo14.lib.number
import kotlin.test.Test
import kotlin.test.assertFails

@Suppress("UNCHECKED_CAST")
class CompiledTest {
	@Test
	fun erasedOnce() {
		textType
			.compiled { "foo" }
			.erasedOnce
			.run {
				value.assertEqualTo("foo")
				assertFails { value }
			}
	}

	@Test
	fun select() {
		emptyType
			.plus("x" lineTo numberType)
			.plus("y" lineTo numberType)
			.compiled { 10.number to 20.number }
			.run {
				select("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo numberType) typed 10.number)
				select("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo numberType) typed 20.number)
				select("z").assertNull
			}
	}

	@Test
	fun get() {
		emptyType
			.plus("point" lineTo emptyType
				.plus("x" lineTo numberType)
				.plus("y" lineTo numberType))
			.compiled { 10.number to 20.number }
			.run {
				get("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo numberType) typed 10.number)
				get("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo numberType) typed 20.number)
				get("z").assertNull
			}
	}

	@Test
	fun matchInfix() {
		textType
			.plus("and" lineTo textType)
			.compiled { "Hello, " to "world!" }
			.matchInfix("and") { rhs ->
				apply<String, String>(rhs, textType) { plus(it) }
			}!!
			.typed
			.assertEqualTo(textType.typed("Hello, world!"))
	}

	@Test
	fun matchInfix_nameMismatch() {
		textType
			.plus("and" lineTo textType)
			.compiled { null!! }
			.matchInfix("or") { null!! }
			.assertNull
	}

	@Test
	fun matchFunction() {
		textType
			.functionTo(numberType)
			.type
			.compiled { fn { (it as String).length.number } }
			.matchFunction { function, fn ->
				ifOrNull(function.from == textType) {
					function.to.compiled { (fn() as Fn)("Hello, world!") }
				}
			}!!
			.typed
			.assertEqualTo(numberType.typed(13.number))
	}
}