package leo14.untyped.typed.lambda

import leo.base.assertEqualTo
import leo13.givenName
import leo14.fieldTo
import leo14.lambda2.at
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.leo
import leo14.script
import leo14.untyped.doesName
import leo14.untyped.isName
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.numberType
import leo14.untyped.typed.textType
import leo14.untyped.typed.type
import org.junit.Test

class CompilerTest {
	@Test
	fun compiledTyped() {
		emptyLibrary
			.plus(script("x") bindingTo 1.typed)
			.plus(script("y") bindingTo 2.typed)
			.plus(script("z") bindingTo 3.typed)
			.compiler(numberType.typed(at(1)))
			.compiledTyped
			.assertEqualTo(numberType.typed(fn(fn(at(1))).invoke(2.typed.term).invoke(3.typed.term)))

	}

	@Test
	fun constantBinding() {
		emptyLibrary
			.compiler(typed("x"))
			.plus(isName fieldTo leo(123))
			.assertEqualTo(emptyLibrary.plus(script("x") bindingTo 123.typed).compiler(emptyTyped))
	}

	@Test
	fun constantBindingAccess() {
		emptyLibrary
			.plus(script("x") bindingTo 1.typed)
			.plus(script("y") bindingTo 2.typed)
			.run {
				applyCompiler(typed("x")).assertEqualTo(compiler(1.typed.type.typed(at(1))))
				applyCompiler(typed("y")).assertEqualTo(compiler(2.typed.type.typed(at(0))))
			}
	}

	@Test
	fun dynamicBinding() {
		emptyLibrary
			.compiler(typed("number"))
			.plus(doesName fieldTo script("given"))
			.assertEqualTo(
				emptyLibrary
					.plus(numberType bindingTo emptyScope.compiled(type(givenName lineTo numberType).typed(at(0))))
					.compiler(emptyTyped))
	}

	@Test
	fun dynamicBindingAccess() {
		emptyLibrary
			.plus(numberType bindingTo emptyScope.compiled(type("first" lineTo numberType).typed(at(0))))
			.plus(textType bindingTo emptyScope.compiled(type("second" lineTo textType).typed(at(0))))
			.run {
				this
					.applyCompiler(123.typed)
					.assertEqualTo(compiler(type("done" lineTo numberType).typed(at(1).invoke(123.typed.term))))

				this
					.applyCompiler("foo".typed)
					.assertEqualTo(compiler(type("done" lineTo numberType).typed(at(0).invoke("foo".typed.term))))
			}
	}
}