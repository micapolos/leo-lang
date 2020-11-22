package leo21.token.evaluator

import leo.base.assertNotNull
import leo14.lambda.fn
import leo14.lambda.value.emptyScope
import leo14.lambda.value.function
import leo14.lambda.value.scope
import leo14.lambda.value.value
import leo21.evaluator.evaluated
import leo21.evaluator.lineTo
import leo21.prim.Prim
import leo21.term.nilTerm
import leo21.token.body.emptyBindings
import leo21.token.body.functionBinding
import leo21.token.body.plus
import leo21.token.type.compiler.emptyLines
import leo21.type.numberType
import leo21.type.lineTo
import leo21.type.type
import kotlin.test.Test

class ContextTest {
	@Test
	fun resolve() {
		Context(
			emptyBindings
				.plus(
					type("x" lineTo numberType, "y" lineTo numberType)
						.functionBinding(type("ok" lineTo type()))),
			emptyLines,
			scope(value(emptyScope<Prim>().function(fn(nilTerm)))))
			.resolve(
				evaluated(
					"x" lineTo evaluated(10.0),
					"y" lineTo evaluated(20.0),
					"point" lineTo evaluated()))
			.assertNotNull
	}
}