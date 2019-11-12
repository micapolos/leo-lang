package leo14.typed.compiler

import leo.base.assertEqualTo
import leo13.stack
import leo14.lambda.term
import leo14.lineTo
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class CompilerTest {
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
		compiler<Any>(
			term("lhs") of type(
				choice(
					"zero" optionTo type("foo"),
					"one" optionTo type("bar"))))
			.compile(
				script(
					"match" lineTo script(
						"zero" lineTo script(
							"zoo" lineTo script(),
							"of" lineTo script("choice" lineTo script("zoo", "zar"))),
						"one" lineTo script(
							"zar" lineTo script(),
							"of" lineTo script("choice" lineTo script("zoo", "zar"))))))
			.assertEqualTo(
				compiler(
					Match<Any>(
						(term("lhs") of type(
							choice(
								"zero" optionTo type("foo"),
								"one" optionTo type("bar")))).term,
						stack(
							"one" optionTo type("bar"),
							"zero" optionTo type("foo")),
						null)
						.begin("zero")
						.updateTyped { this }
						.end()
						.begin("one")
						.updateTyped { this }
						.end()
						.end()))
	}
}