package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class CompilerTest {
	@Test
	fun typeCompilerField() {
		compiler<Any>(emptyType)
			.write("foo" lineTo script())
			.assertEqualTo(compiler(type("foo" lineTo type())))
	}

	@Test
	fun typeCompilerChoice() {
		compiler<Any>(type("foo" lineTo type()))
			.write(
				"choice" lineTo script(
					"zero" lineTo script("foo"),
					"one" lineTo script("bar")))
			.assertEqualTo(
				compiler(
					type(
						"foo" lineTo type(),
						line(choice(
							"zero" caseTo type("foo"),
							"one" caseTo type("bar"))))))
	}

	@Test
	fun typedCompilerField() {
		compiler<Any>(emptyTyped())
			.write("zero" lineTo script())
			.assertEqualTo(compiler(emptyTyped<Any>().plus(line("zero" fieldTo emptyTyped()))))
	}

	@Test
	fun typedCompilerOf() {
		compiler<Any>(typed(line("zero" fieldTo emptyTyped())))
			.write(
				script(
					"of" lineTo script(
						"choice" lineTo script(
							"zero" lineTo script(),
							"one" lineTo script()))))
			.assertEqualTo(
				compiler(
					typed<Any>(line("zero" fieldTo emptyTyped())).castTypedTo(type(choice("zero", "one")))))
	}
}