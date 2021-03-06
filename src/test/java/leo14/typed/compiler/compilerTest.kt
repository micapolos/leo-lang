package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.*
import leo14.lambda.*
import leo14.native.Native
import leo14.native.native
import leo14.typed.*
import leo14.typed.compiler.natives.compiler
import leo14.typed.compiler.natives.emptyContext
import leo14.typed.compiler.natives.evaluator
import leo14.typed.compiler.natives.typed
import kotlin.test.Test

class CompilerTest {
	@Test
	fun simpleType() {
		compiler(type())
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				compiler(
					type(
						"point" lineTo type(
							"x" lineTo type("zero"),
							"y" lineTo type("one")))))
	}

	@Test
	fun nativeType() {
		compiler(type())
			.parse(script("number" lineTo script()))
			.assertEqualTo(compiler(numberType))
	}

	@Test
	fun choiceType() {
		compiler(type())
			.parse(
				script(
					Keyword.CHOICE.stringIn(defaultLanguage) lineTo script(
						"zero" lineTo script("foo"),
						"one" lineTo script("bar"))))
			.assertEqualTo(
				compiler(
					type(
						line(
							choice(
								"zero" optionTo type("foo"),
								"one" optionTo type("bar"))))))
	}

	@Test
	fun arrowType() {
		compiler(type())
			.parse(
				script(
					Keyword.FUNCTION.stringIn(defaultLanguage) lineTo script(
						"one" lineTo script(),
						"plus" lineTo script("two"),
						Keyword.GIVING.stringIn(defaultLanguage) lineTo script("three"))))
			.assertEqualTo(
				compiler(
					type(
						line(
							type(
								"one" lineTo type(),
								"plus" lineTo type("two")) arrowTo type("three")))))
	}

	@Test
	fun compiledFields() {
		compiler(compiled(typed()))
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				compiler(
					compiled(
						typed(
							"point" fieldTo typed(
								"x" fieldTo typed("zero"),
								"y" fieldTo typed("one"))))))
	}

	@Test
	fun compiledResolve() {
		val compiled = compiled(
			typed(
				"point" lineTo typed(
					"x" lineTo typed(native(10)),
					"y" lineTo typed(native(11)))))

		compiler(compiled)
			.parse(script("x"))
			.assertEqualTo(compiler(compiled.resolve("x" lineTo typed(), emptyContext)))
	}

	@Test
	fun compiledDelete() {
		compiler(compiled(typed("zero")))
			.parse(script(Keyword.DELETE.stringIn(defaultLanguage)))
			.assertEqualTo(compiler(compiled(typed())))
	}

	@Test
	fun compiledAs() {
		compiler(compiled(typed()))
			.parse(
				script(
					"false" lineTo script(),
					Keyword.AS.stringIn(defaultLanguage) lineTo script(
						Keyword.CHOICE.stringIn(defaultLanguage) lineTo script(
							"true", "false"))))
			.assertEqualTo(
				compiler(
					compiled(
						typed<Native>("false") `as` type(choice("true", "false")))))
	}

	@Test
	fun compiledAction() {
		compiler(compiled(typed()))
			.parse(
				script(
					Keyword.FUNCTION.string lineTo script(
						Keyword.NUMBER.string lineTo script(),
						Keyword.GIVES.string lineTo script(literal("foo")))))
			.assertEqualTo(compiler(compiled(fn(term(native("foo"))) of type(line(numberType arrowTo textType)))))
	}

	@Test
	fun compiledDo() {
		val function = fn(term(native("foo"))) of type(line(numberType arrowTo textType))
		compiler(compiled(function))
			.parse(script(Keyword.APPLY.string lineTo script(literal(123))))
			.assertEqualTo(compiler(compiled(function.term.invoke(term(native(123))) of textType)))
	}

	@Test
	fun compiledRememberIs() {
		compiler(compiled(typed("foo")))
			.parse(
				script(
					Keyword.DEFINE.string lineTo script(
						"number" lineTo script(),
						Keyword.IS.string lineTo script(literal("foo")))))
			.assertEqualTo(
				compiler(
					compiled(
						typed("foo"),
						memory(
							item(
								key(numberType),
								value(
									memoryBinding(
										term(native("foo")) of textType,
										isAction = false)))),
						localMemorySize = 1)))
	}

	@Test
	fun compiledFieldRememberIsInvoke() {
		compiler(compiled())
			.parse(
				script(
					"my" lineTo script(
						Keyword.DEFINE.string lineTo script(
							"zero" lineTo script(),
							Keyword.IS.string lineTo script(literal(0))),
						"zero" lineTo script())))
			.assertEqualTo(
				compiler(
					compiled(
						fn(arg0<Native>()).invoke(term(native(0))) of type("my" lineTo numberType),
						memory())))
	}

	@Test
	fun compiledRemindRememberIs() {
		val compiled = compiled(
			typed(),
			memory(
				item(
					key(type("zero")),
					value(
						memoryBinding(
							term(native(1)) of numberType,
							isAction = false)))))

		compiler(compiled)
			.parse(script("zero"))
			.assertEqualTo(
				compiler(
					compiled.updateTyped {
						arg0<Native>() of numberType
					}))
	}

	@Test
	fun evaluateRememberedValue() {
		val compiled = compiled(
			typed(),
			memory(
				item(
					key(type("zero")),
					value(
						memoryBinding(
							term(native(1)) of numberType,
							isAction = false)))))

		evaluator(compiled)
			.parse(script("zero"))
			.assertEqualTo(
				evaluator(
					compiled.updateTyped {
						term(native(1)) of numberType
					}))
	}

	@Test
	fun compiledRememberDoes() {
		compiler(compiled(typed("foo")))
			.parse(
				script(
					Keyword.DEFINE.string lineTo script(
						"number" lineTo script(),
						Keyword.GIVES.string lineTo script(literal("foo")))))
			.assertEqualTo(
				compiler(
					compiled(
						typed("foo"),
						memory(
							item(
								key(numberType),
								value(
									memoryBinding(
										fn(term(native("foo"))) of textType,
										isAction = true)))),
						localMemorySize = 1)))
	}

	@Test
	fun compiledRememberDoesGiven() {
		compiler(compiled(typed("foo")))
			.parse(
				script(
					Keyword.DEFINE.string lineTo script(
						"number" lineTo script(),
						Keyword.GIVES.string lineTo script("given"))))
			.assertEqualTo(
				compiler(
					compiled(
						typed("foo"),
						memory(
							item(
								key(numberType),
								value(
									memoryBinding(
										fn(arg0<Native>()) of type("given" lineTo numberType),
										isAction = true)))),
						localMemorySize = 1)))
	}

	@Test
	fun compiledRememberDoesGivenInvoke() {
		val compiled =
			compiled<Native>(
				typed(),
				memory(
					item(
						key(numberType),
						value(
							memoryBinding(
								typed(arg0(), type("given" lineTo numberType)),
								isAction = true)))))

		compiler(compiled)
			.parse(script(literal(123)))
			.assertEqualTo(
				compiler(
					compiled.updateTyped {
						arg0<Native>().invoke(term(native(123))) of type("given" lineTo numberType)
					}))
	}

	@Test
	fun compiledFieldRememberDoesGivenInvoke() {
		compiler(compiled())
			.parse(
				script(
					"my" lineTo script(
						Keyword.DEFINE.string lineTo script(
							"number" lineTo script(),
							Keyword.GIVES.string lineTo script("given")),
						line(literal(123)))))
			.assertEqualTo(
				compiler(
					compiled(
						fn(arg0<Native>().invoke(term(native(123)))).invoke(id()) of
							type("my" lineTo type("given" lineTo numberType))
					)))
	}

	@Test
	fun compiledRememberedActionDefinition() {
		val compiled = compiled(
			typed(),
			memory(
				item(
					key(numberType),
					value(
						memoryBinding(
							fn(term(native("foo"))) of textType,
							isAction = true)))))

		compiler(compiled)
			.parse(script(literal(0)))
			.assertEqualTo(
				compiler(
					compiled.updateTyped {
						arg0<Native>().invoke(term(native(0))) of textType
					}))
	}

	@Test
	fun evaluateRememberedActionDefinition() {
		val compiled =
			compiled(
				typed(),
				memory(
					item(
						key(type("zero")),
						value(
							memoryBinding(
								fn(term(native(0))) of numberType,
								isAction = true)))))

		evaluator(compiled)
			.parse(script("zero"))
			.assertEqualTo(
				evaluator(
					compiled.updateTyped {
						term(native(0)) of numberType
					}))
	}


	@Test
	fun compiledComment() {
		compiler(compiled(typed()))
			.parse(
				script(
					"comment" lineTo script("one"),
					"zero" lineTo script(),
					"comment" lineTo script("two"),
					"as" lineTo script(
						"comment" lineTo script("three"),
						"choice" lineTo script(
							"comment" lineTo script("four"),
							"zero" lineTo script(),
							"comment" lineTo script("five"),
							"one" lineTo script(),
							"comment" lineTo script("six")),
						"comment" lineTo script("seven"))))
			.assertEqualTo(
				compiler(
					compiled(
						typed<Native>("zero")
							.`as`(type(choice("zero", "one"))))))
	}

	@Test
	fun compileLiteral() {
		compiler(compiled(typed()))
			.parse(script(literal(123)))
			.assertEqualTo(compiler(compiled(typed(native(123)))))
	}

	@Test
	fun compileIntNativePlus() {
		compiler(compiled(typed()))
			.parse(
				script(
					line(literal(123)),
					"plus" lineTo script(literal(123))))
			.assertEqualTo(
				compiler(
					emptyContext.resolve(compiled(typed(native(123)).plus("plus" lineTo typed(native(123)))))!!))
	}

	@Test
	fun evalIntNativePlus() {
		evaluator(compiled())
			.parse(
				script(
					line(literal(2)),
					"plus" lineTo script(literal(3))))
			.assertEqualTo(evaluator(compiled(typed(native(5)))))
	}
}
