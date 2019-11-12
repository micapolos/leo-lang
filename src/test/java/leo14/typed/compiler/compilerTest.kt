package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.lambda.id
import leo14.lambda.term
import leo14.lineTo
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class CompilerTest {
	@Test
	fun structCompiler() {
		compiler(typed<Any>())
			.compile(
				script(
					"vec" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				compiler(
					id<Any>() of type(
						"vec" lineTo type(
							"x" lineTo type("zero"),
							"y" lineTo type("one")))))
	}

	@Test
	fun deleteCompiler() {
		compiler(term("foo") of nativeType)
			.compile("delete" lineTo script())
			.assertEqualTo(compiler(typed()))
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
		compiler<Any>(typed())
			.compile("zero" lineTo script())
			.assertEqualTo(compiler(typed<Any>().plus(line("zero" fieldTo typed()))))
	}

	@Test
	fun typedCompilerOf() {
		compiler<Any>(typed(line("zero" fieldTo typed())))
			.compile(
				script(
					"of" lineTo script(
						"choice" lineTo script(
							"zero" lineTo script(),
							"one" lineTo script()))))
			.assertEqualTo(
				compiler(
					typed<Any>(line("zero" fieldTo typed())).of(type(choice("zero", "one")))))
	}

	@Test
	fun simpleMatch() {
		compiler<Any>(typed())
			.compile(
				script(
					"one" lineTo script(),
					"of" lineTo script("choice" lineTo script("zero", "one")),
					"match" lineTo script(
						"zero" lineTo script(),
						"one" lineTo script())))
			.assertEqualTo(
				compiler(
					typed<Any>("one")
						.of(type(choice("zero", "one")))
						.beginMatch()
						.beginCase("zero")
						.end()
						.beginCase("one")
						.end()
						.end()))
	}

	@Test
	fun typedCompilerMatch() {
		compiler<Any>(typed())
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
			.assertEqualTo(
				compiler(
					typed<Any>()
						.plus("zero" fieldTo typed("foo"))
						.of(type(choice(
							"zero" optionTo type("foo"),
							"one" optionTo type("bar"))))
						.beginMatch()
						.beginCase("zero")
						.update { of(type(choice("foo", "bar"))) }
						.end()
						.beginCase("one")
						.update { of(type(choice("foo", "bar"))) }
						.end()
						.end()))
	}
}