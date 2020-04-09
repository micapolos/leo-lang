package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.ifOrNull
import leo14.begin
import leo14.lambda.runtime.fn
import leo14.literal
import leo14.number
import leo14.untyped.*
import kotlin.test.Test
import kotlin.test.assertFails

@Suppress("UNCHECKED_CAST")
class CompiledTest {
	@Test
	fun plusLiteral() {
		emptyCompiled
			.append(literal(2))
			.evaluate
			.assertEqualTo(numberType.compiled(2.number))
	}

	@Test
	fun plusField() {
		emptyCompiled
			.append(begin("foo"), emptyCompiled)
			.evaluate
			.assertEqualTo(type("foo" lineTo emptyType).compiled(null))
	}

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
			.linkApply(textType) { rhs ->
				asString + rhs.asNumber.toString()
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
				apply(rhs, textType) { asString + it.asString }
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
			.compiled { fn { it.asString.length.number } }
			.assertEvaluatedOnce
			.matchFunction { function, block ->
				ifOrNull(function.from == textType) {
					function.to.compiled { block.value.asFn("Hello, world!") }
				}
			}!!
			.typed
			.assertEqualTo(numberType.typed(13.number))
	}

	@Test
	fun applyNativeClass() {
		emptyType
			.plus(className lineTo emptyType
				.plus(nativeName lineTo textType))
			.compiled("java.lang.String")
			.applyNativeClass!!
			.evaluate
			.assertEqualTo(
				emptyType
					.plus(className lineTo nativeType)
					.compiled(java.lang.String::class.java))
	}

	@Test
	fun applyListOf() {
		type(
			listName lineTo type(
				ofName lineTo type(
					numberName lineTo emptyType)))
			.compiled(null)
			.applyListOf!!
			.assertEqualTo(type(listName lineTo numberType.repeating.toType).compiled(null))
	}
}
