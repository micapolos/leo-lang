package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.lambda.term
import leo14.lineTo
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class CompilerTest {
	@Test
	fun deleteCompiler() {
		compiler(term("foo") of nativeType)
			.compile("delete" lineTo script())
			.assertEqualTo(compiler(emptyTyped()))
	}

	@Test
	fun typeCompilerField() {
		compiler<Any>(emptyType)
			.compile("foo" lineTo script())
			.assertEqualTo(compiler(type("foo" lineTo type())))
	}

	@Test
	fun typeCompilerChoice() {
		compiler<Any>(type("foo" lineTo type()))
			.compile(
				"choice" lineTo script(
					"zero" lineTo script("foo"),
					"one" lineTo script("bar")))
			.assertEqualTo(
				compiler(
					type(
						"foo" lineTo type(),
						line(choice(
							"zero" optionTo type("foo"),
							"one" optionTo type("bar"))))))
	}

	@Test
	fun typedCompilerField() {
		compiler<Any>(emptyTyped())
			.compile("zero" lineTo script())
			.assertEqualTo(compiler(emptyTyped<Any>().plus(line("zero" fieldTo emptyTyped()))))
	}

	@Test
	fun typedCompilerOf() {
		compiler<Any>(typed(line("zero" fieldTo emptyTyped())))
			.compile(
				script(
					"of" lineTo script(
						"choice" lineTo script(
							"zero" lineTo script(),
							"one" lineTo script()))))
			.assertEqualTo(
				compiler(
					typed<Any>(line("zero" fieldTo emptyTyped())).castTypedTo(type(choice("zero", "one")))))
	}

	@Test
	fun typedCompilerMatch() {
		compiler<Any>(emptyTyped())
			.compile(
				script(
					"zero" lineTo script("foo"),
					"of" lineTo script(
						"choice" lineTo script(
							"zero" lineTo script("foo"),
							"one" lineTo script("bar"))),
					"match" lineTo script(
						"zero" lineTo script(
							"of" lineTo script(
								"choice" lineTo script("foo", "bar"))),
						"one" lineTo script(
							"of" lineTo script(
								"choice" lineTo script("foo", "bar"))))))
			.assertEqualTo(null)
	}
}