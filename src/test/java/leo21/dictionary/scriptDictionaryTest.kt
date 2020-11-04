package leo21.dictionary

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.compiled.emptyBindings
import leo21.type.arrowTo
import leo21.type.doubleType
import leo21.type.lineTo
import leo21.type.type
import leo21.typed.ArrowTyped
import leo21.typed.typed
import kotlin.test.Test

class ScriptDictionaryTest {
	@Test
	fun all() {
		emptyBindings
			.dictionary(
				script(
					// TODO: We don't want "defines" at the top level.
					"define" lineTo script(
						"x" lineTo script("number"),
						"does" lineTo script("given")),
					"define" lineTo script(
						"y" lineTo script(),
						"gives" lineTo script(literal(10.0)))))
			.assertEqualTo(
				emptyDictionary
					.plus(
						definition(
							ArrowTyped(
								fn(arg(0)),
								type("x" lineTo doubleType) arrowTo type("given" lineTo type("x" lineTo doubleType)))))
					.plus(
						definition(
							type("y" lineTo type()),
							typed(10.0))))
	}
}