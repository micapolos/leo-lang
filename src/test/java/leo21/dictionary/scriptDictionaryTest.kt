package leo21.dictionary

import leo.base.assertEqualTo
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lineTo
import leo14.literal
import leo14.script
import leo21.compiled.ArrowCompiled
import leo21.compiled.compiled
import leo21.compiler.emptyBindings
import leo21.type.arrowTo
import leo21.type.numberType
import leo21.type.lineTo
import leo21.type.type
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
							ArrowCompiled(
								fn(arg(0)),
								type("x" lineTo numberType) arrowTo type("given" lineTo type("x" lineTo numberType)))))
					.plus(
						definition(
							type("y" lineTo type()),
							compiled(10.0))))
	}
}