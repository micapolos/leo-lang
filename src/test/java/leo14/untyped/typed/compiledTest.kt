package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.ifOrNull
import leo14.begin
import leo14.bigDecimal
import leo14.lambda.runtime.fn
import leo14.literal
import leo14.untyped.listName
import leo14.untyped.nativeName
import leo14.untyped.ofName
import kotlin.test.Test
import kotlin.test.assertFails

@Suppress("UNCHECKED_CAST")
class CompiledTest {
	@Test
	fun plusLiteral() {
		emptyCompiled
			.append(literal("foo"))
			.evaluate
			.assertEqualTo(textType.compiled("foo"))

		emptyCompiled
			.append(literal(2))
			.evaluate
			.assertEqualTo(numberType.compiled(2.bigDecimal))
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
		nativeType
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
			.plus(nativeTypeLine)
			.plus(nativeTypeLine)
			.compiled { "number: " to 10 }
			.assertEvaluatedOnce
			.linkApply(nativeType) { rhs ->
				asString + rhs.asInt.toString()
			}!!
			.typed
			.assertEqualTo(nativeType typed "number: 10")
	}

	@Test
	fun select() {
		emptyType
			.plus("x" lineTo nativeType)
			.plus("y" lineTo nativeType)
			.compiled { 10 to 20 }
			.run {
				assertEvaluatedOnce.select("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo nativeType) typed 10)
				assertEvaluatedOnce.select("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo nativeType) typed 20)
				assertEvaluatedOnce.select("z").assertNull
			}
	}

	@Test
	fun get() {
		emptyType
			.plus("point" lineTo emptyType
				.plus("x" lineTo nativeType)
				.plus("y" lineTo nativeType))
			.compiled { 10 to 20 }
			.run {
				assertEvaluatedOnce.get("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo nativeType) typed 10)
				assertEvaluatedOnce.get("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo nativeType) typed 20)
				assertEvaluatedOnce.get("z").assertNull
			}
	}

	@Test
	fun matchInfix() {
		nativeType
			.plus("and" lineTo nativeType)
			.compiled { "Hello, " to "world!" }
			// TODO: Fixit!!!
			//.assertEvaluatedOnce
			.matchInfix("and") { rhs ->
				apply(rhs, nativeType) { asString + it.asString }
			}!!
			.typed
			.assertEqualTo(nativeType.typed("Hello, world!"))
	}

	@Test
	fun matchInfix_nameMismatch() {
		nativeType
			.plus("and" lineTo nativeType)
			.compiled { null!! }
			.assertEvaluatedOnce
			.matchInfix("or") { null!! }
			.assertNull
	}

	@Test
	fun matchFunction() {
		nativeType
			.functionTo(nativeType)
			.type
			.compiled { fn { it.asString.length } }
			.assertEvaluatedOnce
			.matchFunction { function, block ->
				ifOrNull(function.from == nativeType) {
					function.to.compiled { block.value.asFn("Hello, world!") }
				}
			}!!
			.typed
			.assertEqualTo(nativeType.typed(13))
	}

	@Test
	fun applyListOf() {
		type(
			listName lineTo type(
				ofName lineTo type(
					nativeName lineTo emptyType)))
			.compiled(null)
			.applyListOf!!
			.assertEqualTo(type(listName lineTo nativeType.repeating.toType).compiled(null))
	}
}
