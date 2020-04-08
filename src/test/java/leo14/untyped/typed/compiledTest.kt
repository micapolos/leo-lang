package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.ifOrNull
import leo14.lambda.runtime.Fn
import leo14.lambda.runtime.fn
import leo14.lib.Number
import leo14.lib.number
import kotlin.test.Test
import kotlin.test.assertFails

@Suppress("UNCHECKED_CAST")
class CompiledTest {
	@Test
	fun erasedOnce() {
		textType
			.compiled { "foo" }
			.assertEvaluatedOnce
			.run {
				value.assertEqualTo("foo")
				assertFails { value }
			}
	}

	@Test
	fun linkApply() {
		emptyType
			.plus(textTypeLine)
			.plus(numberTypeLine)
			.compiled { "number: " to 10.number }
			.assertEvaluatedOnce
			.linkApply<String, Number, String>(textType) { number ->
				this + number.toString()
			}!!
			.typed
			.assertEqualTo(textType typed "number: 10")
	}

	@Test
	fun select() {
		emptyType
			.plus("x" lineTo numberType)
			.plus("y" lineTo numberType)
			.compiled { 10.number to 20.number }
			.run {
				assertEvaluatedOnce.select("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo numberType) typed 10.number)
				assertEvaluatedOnce.select("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo numberType) typed 20.number)
				assertEvaluatedOnce.select("z").assertNull
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
				assertEvaluatedOnce.get("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo numberType) typed 10.number)
				assertEvaluatedOnce.get("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo numberType) typed 20.number)
				assertEvaluatedOnce.get("z").assertNull
			}
	}

	@Test
	fun matchInfix() {
		textType
			.plus("and" lineTo textType)
			.compiled { "Hello, " to "world!" }
			// TODO: Fixit!!!
			//.assertEvaluatedOnce
			.matchInfix("and") { rhs ->
				apply(rhs, textType) { (this as String) + (it as String) }
			}!!
			.typed
			.assertEqualTo(textType.typed("Hello, world!"))
	}

	@Test
	fun matchInfix_nameMismatch() {
		textType
			.plus("and" lineTo textType)
			.compiled { null!! }
			.assertEvaluatedOnce
			.matchInfix("or") { null!! }
			.assertNull
	}

	@Test
	fun matchFunction() {
		textType
			.functionTo(numberType)
			.type
			.compiled { fn { (it as String).length.number } }
			.assertEvaluatedOnce
			.matchFunction { function, block ->
				ifOrNull(function.from == textType) {
					function.to.compiled { (block.value as Fn)("Hello, world!") }
				}
			}!!
			.typed
			.assertEqualTo(numberType.typed(13.number))
	}
}