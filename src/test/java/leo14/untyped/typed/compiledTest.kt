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
		javaType
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
			.plus(javaTypeLine)
			.plus(javaTypeLine)
			.compiled { "number: " to 10 }
			.assertEvaluatedOnce
			.linkApply(javaType) { rhs ->
				asString + rhs.asInt.toString()
			}!!
			.typed
			.assertEqualTo(javaType typed "number: 10")
	}

	@Test
	fun select() {
		emptyType
			.plus("x" lineTo javaType)
			.plus("y" lineTo javaType)
			.compiled { 10 to 20 }
			.run {
				assertEvaluatedOnce.select("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo javaType) typed 10)
				assertEvaluatedOnce.select("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo javaType) typed 20)
				assertEvaluatedOnce.select("z").assertNull
			}
	}

	@Test
	fun get() {
		emptyType
			.plus("point" lineTo emptyType
				.plus("x" lineTo javaType)
				.plus("y" lineTo javaType))
			.compiled { 10 to 20 }
			.run {
				assertEvaluatedOnce.get("x")!!.typed.assertEqualTo(emptyType.plus("x" lineTo javaType) typed 10)
				assertEvaluatedOnce.get("y")!!.typed.assertEqualTo(emptyType.plus("y" lineTo javaType) typed 20)
				assertEvaluatedOnce.get("z").assertNull
			}
	}

	@Test
	fun matchInfix() {
		javaType
			.plus("and" lineTo javaType)
			.compiled { "Hello, " to "world!" }
			// TODO: Fixit!!!
			//.assertEvaluatedOnce
			.matchInfix("and") { rhs ->
				apply(rhs, javaType) { asString + it.asString }
			}!!
			.typed
			.assertEqualTo(javaType.typed("Hello, world!"))
	}

	@Test
	fun matchInfix_nameMismatch() {
		javaType
			.plus("and" lineTo javaType)
			.compiled { null!! }
			.assertEvaluatedOnce
			.matchInfix("or") { null!! }
			.assertNull
	}

	@Test
	fun matchFunction() {
		javaType
			.functionTo(javaType)
			.type
			.compiled { fn { it.asString.length } }
			.assertEvaluatedOnce
			.matchFunction { function, block ->
				ifOrNull(function.from == javaType) {
					function.to.compiled { block.value.asFn("Hello, world!") }
				}
			}!!
			.typed
			.assertEqualTo(javaType.typed(13))
	}

	@Test
	fun applyListOf() {
		type(
			listName lineTo type(
				ofName lineTo type(
					nativeName lineTo emptyType)))
			.compiled(null)
			.applyListOf!!
			.assertEqualTo(type(listName lineTo javaType.repeating.toType).compiled(null))
	}
}
