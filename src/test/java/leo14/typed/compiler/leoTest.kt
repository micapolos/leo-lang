package leo14.typed.compiler

import leo.base.assertEqualTo
import leo13.index0
import leo14.*
import leo14.lambda.arg0
import leo14.lambda.id
import leo14.lambda.invoke
import leo14.native.Native
import leo14.native.native
import leo14.typed.*
import leo14.typed.compiler.natives.context
import kotlin.test.Test

class TypeParserTest {
	@Test
	fun simpleType() {
		leo14.typed.compiler.natives.compiler(type())
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					type(
						"point" lineTo type(
							"x" lineTo type("zero"),
							"y" lineTo type("one")))))
	}

	@Test
	fun nativeType() {
		leo14.typed.compiler.natives.compiler(type())
			.parse(script(defaultDictionary.native lineTo script()))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(nativeType))
	}

	@Test
	fun choiceType() {
		leo14.typed.compiler.natives.compiler(type())
			.parse(
				script(
					defaultDictionary.choice lineTo script(
						"zero" lineTo script("foo"),
						"one" lineTo script("bar"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					type(
						line(
							choice(
								"zero" optionTo type("foo"),
								"one" optionTo type("bar"))))))
	}

	@Test
	fun arrowType() {
		leo14.typed.compiler.natives.compiler(type())
			.parse(
				script(
					defaultDictionary.action lineTo script(
						"one" lineTo script(),
						"plus" lineTo script("two"),
						defaultDictionary.giving lineTo script("three"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					type(
						line(
							type(
								"one" lineTo type(),
								"plus" lineTo type("two")) arrowTo type("three")))))
	}

	@Test
	fun compiledFields() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed(
							"point" fieldTo typed(
								"x" fieldTo typed("zero"),
								"y" fieldTo typed("one"))))))
	}

	@Test
	fun compiledNothing() {
		leo14.typed.compiler.natives.compiler(compiled(typed("zero")))
			.parse(script("nothing"))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed("zero"))))
	}

	@Test
	fun compiledResolve() {
		val compiled = compiled(
			typed(
				"point" lineTo typed(
					"x" lineTo typed(native(10)),
					"y" lineTo typed(native(11)))))

		leo14.typed.compiler.natives.compiler(compiled)
			.parse(script("x"))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled.resolve("x" lineTo typed(), context)))
	}

	@Test
	fun compiledDelete() {
		leo14.typed.compiler.natives.compiler(compiled(typed("zero")))
			.parse(script(defaultDictionary.delete))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed())))
	}

	@Test
	fun compiledGive() {
		leo14.typed.compiler.natives.compiler(compiled(typed("zero")))
			.parse(
				script(
					defaultDictionary.give lineTo script(
						"one" lineTo script())))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed("one"))))
	}

	@Test
	fun compiledAs() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
			.parse(
				script(
					"false" lineTo script(),
					defaultDictionary.`as` lineTo script(
						defaultDictionary.choice lineTo script(
							"true", "false"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed<Native>("false") `as` type(choice("true", "false")))))
	}

	@Test
	fun compiledAction() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
			.parse(
				script(
					defaultDictionary.action lineTo script(
						"zero" lineTo script(),
						defaultDictionary.does lineTo script(
							"plus" lineTo script("one")))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed<Native>()
							.plus(type("zero") does typed(
								"given" lineTo typed("zero"),
								"plus" lineTo typed("one"))))))
	}

	@Test
	fun compiledDo() {
		val action = type("zero") does typed<Native>("one")
		leo14.typed.compiler.natives.compiler(compiled(typed(action)))
			.parse(script("do" lineTo script("zero")))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(action.resolve(typed("zero"))!!)))
	}

	@Test
	fun compiledRememberIs() {
		leo14.typed.compiler.natives.compiler(compiled(typed("foo")))
			.parse(
				script(
					"remember" lineTo script(
						"zero" lineTo script(),
						"is" lineTo script("one"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed("foo"),
						memory(
							remember(
								type("zero") does typed("one"),
								needsInvoke = false)))))
	}

	@Test
	fun compiledRemindRememberIs() {
		val compiled = compiled<Native>(
			typed(),
			memory(
				remember(
					type("zero") does typed("one"),
					needsInvoke = false)))

		leo14.typed.compiler.natives.compiler(compiled)
			.parse(script("zero"))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled.updateTyped { typed(index0, type("one")) }))
	}

	@Test
	fun compiledRememberDoes() {
		leo14.typed.compiler.natives.compiler(compiled(typed("foo")))
			.parse(
				script(
					"remember" lineTo script(
						"zero" lineTo script(),
						"does" lineTo script("done"))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed("foo"),
						memory(
							remember(
								type("zero") does typed<Native>(id(), type("zero")).resolveWrap("done"),
								needsInvoke = true)))))
	}

	@Test
	fun compiledRemindRememberDoes() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
			.parse(
				script(
					"remember" lineTo script(
						"zero" lineTo script(),
						"does" lineTo script("done")),
					"zero" lineTo script()))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed(arg0<Native>().invoke(typed<Native>("zero").term), type("zero")).resolveWrap("done"),
						memory(
							remember(
								type("zero") does typed<Native>(id(), type("zero")).resolveWrap("done"),
								needsInvoke = true)))))
	}

	@Test
	fun compiledForget_match() {
		leo14.typed.compiler.natives.compiler(
			compiled(
				typed(),
				memory(remember(type("zero") does typed()))))
			.parse(script("forget" lineTo script("zero")))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed(), memory())))
	}

	@Test
	fun compiledForget_mismatch() {
		leo14.typed.compiler.natives.compiler(
			compiled(
				typed(),
				memory(remember(type("zero") does typed()))))
			.parse(script("forget" lineTo script("one")))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed(), memory(remember(type("zero") does typed())))))
	}

	@Test
	fun compiledComment() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
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
				leo14.typed.compiler.natives.compiler(
					compiled(
						typed<Native>("zero")
							.`as`(type(choice("zero", "one"))))))
	}

	@Test
	fun compileLiteral() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
			.parse(script(literal(123)))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed(native(123)))))
	}

	@Test
	fun compileIntNativePlus() {
		leo14.typed.compiler.natives.compiler(compiled(typed()))
			.parse(
				script(
					line(literal(123)),
					"plus" lineTo script(literal(123))))
			.assertEqualTo(
				leo14.typed.compiler.natives.compiler(
					compiled(
						context.resolve(typed(native(123)).plus("plus" lineTo typed(native(123))))!!)))
	}

	@Test
	fun evalIntNativePlus() {
		leo14.typed.compiler.natives.compiler(compiled(typed()), Phase.EVALUATOR)
			.parse(
				script(
					line(literal(2)),
					"plus" lineTo script(literal(3))))
			.assertEqualTo(leo14.typed.compiler.natives.compiler(compiled(typed(native(5))), Phase.EVALUATOR))
	}
}
