package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo14.fieldTo
import leo14.lambda2.at
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.leo
import leo14.untyped.isName
import leo14.untyped.typed.numberType
import leo14.untyped.typed.type
import org.junit.Test

class CompilerTest {
	@Test
	fun compiledTyped() {
		emptyLibrary
			.plus(type("x") bindingTo 1.typed)
			.plus(type("y") bindingTo 2.typed)
			.plus(type("z") bindingTo 3.typed)
			.compiler(numberType.typed(at(1)))
			.compiledTyped
			.assertEqualTo(numberType.typed(fn(fn(at(1))).invoke(2.typed.term).invoke(3.typed.term)))

	}

	@Test
	fun typeIsTyped() {
		emptyLibrary
			.compiler(typed("x"))
			.plus(isName fieldTo leo(123))
			.assertEqualTo(emptyLibrary.plus(type("x") bindingTo 123.typed).compiler(emptyTyped))
	}

	@Test
	fun entryAccess() {
		emptyLibrary
			.plus(type("x") bindingTo 1.typed)
			.plus(type("y") bindingTo 2.typed)
			.run {
				applyCompiler(typed("x")).assertEqualTo(compiler(1.typed.type.typed(at(1))))
				applyCompiler(typed("y")).assertEqualTo(compiler(2.typed.type.typed(at(0))))
			}
	}
}