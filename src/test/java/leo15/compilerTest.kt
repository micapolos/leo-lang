package leo15

import leo.base.assertEqualTo
import leo13.givenName
import leo14.fieldTo
import leo14.invoke
import leo14.lambda2.at
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.leo
import leo14.script
import org.junit.Test

class CompilerTest {
	@Test
	fun give() {
		emptyLibrary
			.compiler(10.typed)
			.plus(giveName fieldTo script(givenName))
			.assertEqualTo(
				emptyLibrary.compiler(type(givenName lineTo numberType).typed(fn(at(0)).invoke(10.typed.term))))
	}

	@Test
	fun compiledTyped() {
		emptyLibrary
			.plus(script("x") bindingTo 1.typed)
			.plus(script("y") bindingTo 2.typed)
			.plus(script("z") bindingTo 3.typed)
			.compiler(numberType.typed(at(1)))
			.compiledTyped
			.assertEqualTo(
				numberType
					.typed(
						fn(
							fn(
								fn(at(1))
									.invoke(3.typed.term))
								.invoke(2.typed.term))
							.invoke(1.typed.term)))
	}

	@Test
	fun is_() {
		emptyLibrary
			.compiler(typed("x"))
			.plus(isName fieldTo leo(10))
			.assertEqualTo(emptyLibrary.plus(script("x") bindingTo 10.typed).compiler(emptyTyped))
	}


	@Test
	fun is_after_is() {
		emptyLibrary
			.plus(script("x") bindingTo 10.typed)
			.compiler(typed("y"))
			.plus(isName fieldTo leo("x"()))
			.assertEqualTo(
				emptyLibrary
					.plus(script("x") bindingTo 10.typed)
					.plus(script("y") bindingTo numberType.typed(at(0)))
					.compiler(emptyTyped))
	}

	@Test
	fun is_access() {
		emptyLibrary
			.plus(script("x") bindingTo 1.typed)
			.plus(script("y") bindingTo 2.typed)
			.run {
				applyCompiler(typed("x")).assertEqualTo(compiler(1.typed.type.typed(at(1))))
				applyCompiler(typed("y")).assertEqualTo(compiler(2.typed.type.typed(at(0))))
			}
	}

	@Test
	fun gives() {
		emptyLibrary
			.compiler(typed("number"))
			.plus(givesName fieldTo script("given"))
			.assertEqualTo(
				emptyLibrary
					.plus(numberType bindingTo emptyScope
						.compiled(
							typed("given" lineTo numberType.typed(at(0))).withFnTerm))
					.compiler(emptyTyped))
	}

	@Test
	fun gives_access() {
		emptyLibrary
			.plus(numberType bindingTo emptyScope.compiled(type(givenName lineTo numberType).typed(fn(at(0)))))
			.plus(textType bindingTo emptyScope.compiled("foo".typed))
			.plus(type("foo") bindingTo emptyScope.compiled(typed("bar")))
			.run {
				applyCompiler(123.typed)
					.assertEqualTo(compiler(type("given" lineTo numberType).typed(at(2).invoke(123.typed.term))))

				applyCompiler("foo".typed)
					.assertEqualTo(compiler(textType.typed(at(1).invoke("foo".typed.term))))

				applyCompiler(typed("foo"))
					.assertEqualTo(compiler(type("bar").typed(at(0).invoke(typed("bar").term))))
			}
	}
}