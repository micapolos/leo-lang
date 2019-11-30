package leo14.typed.compiler

import leo.base.assertEqualTo
import leo13.index1
import leo14.lambda.arg0
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.term
import leo14.native.Native
import leo14.native.native
import leo14.typed.*
import leo14.typed.compiler.natives.context
import kotlin.test.Test

class CompiledTest {
	@Test
	fun resolve() {
		val compiled = compiled(typed(native(0)))
		compiled
			.resolve("x" lineTo typed(), context)
			.assertEqualTo(compiled.updateTyped { plus("x" lineTo typed()).resolve!! })
	}

	@Test
	fun typedForEval() {
		compiled(
			arg0<Native>() of numberType,
			memory(
				item(
					key(type("zero")),
					value(memoryBinding(term(native(1)) of numberType, isAction = false)))))
			.typedForEval
			.assertEqualTo(fn(arg0<Native>()).invoke(term(native(1))) of numberType)
	}

	@Test
	fun typedForEnd() {
		compiled(
			arg0<Native>() of numberType,
			memory(
				item(
					key(type("zero")),
					value(memoryBinding(term(native(1)) of numberType, isAction = false)))),
			index1)
			.typedForEnd
			.assertEqualTo(fn(arg0<Native>()).invoke(term(native(1))) of numberType)
	}
}