package leo21.evaluated

import leo.base.assertEqualTo
import leo14.lambda.nativeTerm
import leo14.lambda.value.function
import leo14.lambda.value.scope
import leo14.lambda.value.value
import leo21.compiled.compiled
import leo21.compiled.does
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.arrowTo
import leo21.type.line
import leo21.type.numberType
import leo21.type.stringType
import leo21.type.type
import kotlin.test.Test

class CompiledTest {
	@Test
	fun text() {
		evaluated("ok")
			.compiled
			.assertEqualTo(compiled("ok"))
	}

	@Test
	fun function() {
		scope<Prim>()
			.function(nativeTerm(prim("ok")))
			.value
			.of(type(line(numberType arrowTo stringType)))
			.compiled
			.assertEqualTo(compiled(numberType does compiled("ok")))
	}
}