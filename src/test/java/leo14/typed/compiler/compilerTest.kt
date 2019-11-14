package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.lambda.arg0
import leo14.lambda.fn
import leo14.lambda.id
import leo14.lambda.term
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class CompilerTest {
	@Test
	fun nativeCompiler() {
		compiler(typed())
			.compile(script(literal("zero")))
			.assertEqualTo(compiler(anyTyped(literal("zero"))))
	}

	@Test
	fun nativePlusNativeCompiler() {
		compiler(arg0<Any>() of nativeType)
			.compile(script(literal("one")))
			.assertEqualTo(compiler(arg0<Any>().of(nativeType).plus(term<Any>("one") of nativeLine)))
	}

	@Test
	fun nativeTypeCompiler() {
		compiler(type())
			.compile(script("native"))
			.assertEqualTo(compiler(nativeType))
	}

	@Test
	fun structCompiler() {
		compiler(typed())
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
	fun giveCompiler() {
		compiler(term("foo") of nativeType)
			.compile("give" lineTo script("one"))
			.assertEqualTo(compiler(typed("one")))
	}

	@Test
	fun typeCompilerField() {
		compiler(emptyType)
			.compile("foo" lineTo script())
			.assertEqualTo(compiler(type("foo" lineTo type())))
	}

	@Test
	fun typeCompilerChoice() {
		compiler(type("foo" lineTo type()))
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
		compiler(typed())
			.compile("zero" lineTo script())
			.assertEqualTo(compiler(typed<Any>().plus(line("zero" fieldTo typed()))))
	}

	@Test
	fun typedCompilerOf() {
		compiler(typed(line("zero" fieldTo typed())))
			.compile(
				script(
					"as" lineTo script(
						"choice" lineTo script(
							"zero" lineTo script(),
							"one" lineTo script()))))
			.assertEqualTo(
				compiler(
					typed<Any>(line("zero" fieldTo typed())).of(type(choice("zero", "one")))))
	}

	@Test
	fun simpleMatch() {
		compiler(typed())
			.compile(
				script(
					"one" lineTo script(),
					"as" lineTo script("choice" lineTo script("zero", "one")),
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
		compiler(typed())
			.compile(
				script(
					"zero" lineTo script("foo"),
					"as" lineTo script(
						"choice" lineTo script(
							"zero" lineTo script("foo"),
							"one" lineTo script("bar"))),
					"match" lineTo script(
						"zero" lineTo script(
							"as" lineTo script(
								"choice" lineTo script("foo", "bar"))),
						"one" lineTo script(
							"as" lineTo script(
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

	@Test
	fun action() {
		compiler(typed())
			.compile(
				script(
					"action" lineTo script(
						"it" lineTo script("zero"),
						"does" lineTo script("plus" lineTo script("one")))))
			.assertEqualTo(
				compiler(id<Any>() of
					type(
						line(type("zero") arrowTo type(
							"zero" lineTo type(),
							"plus" lineTo type("one"))))))
	}

	@Test
	fun actionRememberDo() {
		val context = anyContext(
			memory(
				remember(type("zero") does (fn(arg0<Any>()) of type("one")))))
		compiler(typed(), context)
			.compile(script("zero"))
			.assertEqualTo(
				compiler(
					context.plus(typed(), "zero" fieldTo typed()),
					context))
	}

	@Test
	fun rememberItIs() {
		compiler(typed())
			.compile(
				script(
					"remember" lineTo script(
						"it" lineTo script("zero"),
						"is" lineTo script("one"))))
			.assertEqualTo(
				compiler(
					typed(),
					anyContext(
						memory(
							remember(
								type("zero") does typed("one"),
								needsInvoke = false)))))
	}

	@Test
	fun remindThing() {
		val compiler = compiler(
			typed(),
			anyContext(
				memory(
					remember(
						type("zero") does typed("one"),
						needsInvoke = false))))

		compiler
			.compile(script("zero"))
			.assertEqualTo(
				compiler.copy(typed = compiler.context.resolve(typed("zero"))!!))
	}

	@Test
	fun rememberItDoes() {
		compiler(typed())
			.compile(
				script(
					"remember" lineTo script(
						"it" lineTo script("zero"),
						"does" lineTo script("plus" lineTo script("one")))))
			.assertEqualTo(
				compiler(
					typed(),
					anyContext(
						memory(
							remember(
								type("zero") does typed("zero" fieldTo typed(), "plus" fieldTo typed("one")),
								needsInvoke = true)))))
	}
}