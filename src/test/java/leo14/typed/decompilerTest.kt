package leo14.typed

import leo.base.assertEqualTo
import leo13.stack
import leo14.*
import leo14.lambda.arg1
import leo14.lambda.fn
import leo14.lambda.id
import leo14.lambda.invoke
import kotlin.test.Test

val Script.assertDecompiles
	get() =
		typedCompiler(stack(), { it.any }, ret())
			.compile<Typed<Any>>(this)
			.decompile { line(anyLiteral) }
			.assertEqualTo(this)

class DecompilerTest {
	@Test
	fun struct() {
		script(
			"vec" lineTo script(
				"x" lineTo script(literal(1)),
				"y" lineTo script(literal(2))))
			.assertDecompiles
	}

	@Test
	fun choice() {
		fn(fn(arg1<Any>().invoke(id())))
			.of(choice("zero", "one"))
			.decompileLine { line(anyLiteral) }
			.assertEqualTo("zero" lineTo script())
	}
}