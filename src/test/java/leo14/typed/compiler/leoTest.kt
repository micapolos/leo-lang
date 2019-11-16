package leo14.typed.compiler

import leo.base.assertEqualTo
import leo14.lineTo
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
	fun fields() {
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
}
