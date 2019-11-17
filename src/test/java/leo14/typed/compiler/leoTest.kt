package leo14.typed.compiler

import leo.base.assertEqualTo
import leo13.index0
import leo14.lambda.arg0
import leo14.lambda.id
import leo14.lambda.invoke
import leo14.lineTo
import leo14.native.Native
import leo14.native.native
import leo14.script
import leo14.typed.*
import kotlin.test.Test

class TypeParserTest {
	@Test
	fun simpleType() {
		leo(type())
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				leo(
					type(
						"point" lineTo type(
							"x" lineTo type("zero"),
							"y" lineTo type("one")))))
	}

	@Test
	fun nativeType() {
		leo(type())
			.parse(script(defaultDictionary.native lineTo script()))
			.assertEqualTo(leo(nativeType))
	}

	@Test
	fun choiceType() {
		leo(type())
			.parse(
				script(
					defaultDictionary.choice lineTo script(
						"zero" lineTo script("foo"),
						"one" lineTo script("bar"))))
			.assertEqualTo(
				leo(
					type(
						line(
							choice(
								"zero" optionTo type("foo"),
								"one" optionTo type("bar"))))))
	}

	@Test
	fun arrowType() {
		leo(type())
			.parse(
				script(
					defaultDictionary.action lineTo script(
						"one" lineTo script(),
						"plus" lineTo script("two"),
						defaultDictionary.giving lineTo script("three"))))
			.assertEqualTo(
				leo(
					type(
						line(
							type(
								"one" lineTo type(),
								"plus" lineTo type("two")) arrowTo type("three")))))
	}

	@Test
	fun compiledFields() {
		leo(compiled(typed()))
			.parse(
				script(
					"point" lineTo script(
						"x" lineTo script("zero"),
						"y" lineTo script("one"))))
			.assertEqualTo(
				leo(
					compiled(
						typed(
							"point" fieldTo typed(
								"x" fieldTo typed("zero"),
								"y" fieldTo typed("one"))))))
	}

	@Test
	fun compiledNothing() {
		leo(compiled(typed("zero")))
			.parse(script("nothing"))
			.assertEqualTo(leo(compiled(typed("zero"))))
	}

	@Test
	fun compiledResolve() {
		val compiled = compiled(
			typed(
				"point" lineTo typed(
					"x" lineTo typed(native(10)),
					"y" lineTo typed(native(11)))))

		leo(compiled)
			.parse(script("x"))
			.assertEqualTo(leo(compiled.resolve("x" lineTo typed())))
	}

	@Test
	fun compiledDelete() {
		leo(compiled(typed("zero")))
			.parse(script(defaultDictionary.delete))
			.assertEqualTo(leo(compiled(typed())))
	}

	@Test
	fun compiledGive() {
		leo(compiled(typed("zero")))
			.parse(
				script(
					defaultDictionary.give lineTo script(
						"one" lineTo script())))
			.assertEqualTo(leo(compiled(typed("one"))))
	}

	@Test
	fun compiledAs() {
		leo(compiled(typed()))
			.parse(
				script(
					"false" lineTo script(),
					defaultDictionary.`as` lineTo script(
						defaultDictionary.choice lineTo script(
							"true", "false"))))
			.assertEqualTo(
				leo(
					compiled(
						typed<Native>("false") `as` type(choice("true", "false")))))
	}

	@Test
	fun compiledAction() {
		leo(compiled(typed()))
			.parse(
				script(
					defaultDictionary.action lineTo script(
						"zero" lineTo script(),
						defaultDictionary.does lineTo script(
							"plus" lineTo script("one")))))
			.assertEqualTo(
				leo(
					compiled(
						typed<Native>()
							.plus(type("zero") does typed(
								"zero" lineTo typed(),
								"plus" lineTo typed("one"))))))
	}

	@Test
	fun compiledDo() {
		val action = type("zero") does typed<Native>("one")
		leo(compiled(typed(action)))
			.parse(script("do" lineTo script("zero")))
			.assertEqualTo(leo(compiled(action.resolve(typed("zero"))!!)))
	}

	@Test
	fun compiledRememberIs() {
		leo(compiled(typed("foo")))
			.parse(
				script(
					"remember" lineTo script(
						"zero" lineTo script(),
						"is" lineTo script("one"))))
			.assertEqualTo(
				leo(
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

		leo(compiled)
			.parse(script("zero"))
			.assertEqualTo(leo(compiled.updateTyped { typed(index0, type("one")) }))
	}

	@Test
	fun compiledRememberDoes() {
		leo(compiled(typed("foo")))
			.parse(
				script(
					"remember" lineTo script(
						"zero" lineTo script(),
						"does" lineTo script("done"))))
			.assertEqualTo(
				leo(
					compiled(
						typed("foo"),
						memory(
							remember(
								type("zero") does typed<Native>(id(), type("zero")).resolveWrap("done"),
								needsInvoke = true)))))
	}

	@Test
	fun compiledRemindRememberDoes() {
		leo(compiled(typed()))
			.parse(
				script(
					"remember" lineTo script(
						"zero" lineTo script(),
						"does" lineTo script("done")),
					"zero" lineTo script()))
			.assertEqualTo(
				leo(
					compiled(
						typed(arg0<Native>().invoke(typed<Native>("zero").term), type("zero")).resolveWrap("done"),
						memory(
							remember(
								type("zero") does typed<Native>(id(), type("zero")).resolveWrap("done"),
								needsInvoke = true)))))
	}

	@Test
	fun compiledForget() {
		leo(compiled(typed()))
			.parse(script("forget" lineTo script("zero")))
			.assertEqualTo(leo(compiled(typed(), memory(forget(type("zero"))))))
	}

	@Test
	fun compiledComment() {
		leo(compiled(typed()))
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
				leo(
					compiled(
						typed<Native>("zero")
							.`as`(type(choice("zero", "one"))))))
	}
}
