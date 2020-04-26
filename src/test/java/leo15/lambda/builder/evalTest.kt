package leo15.lambda.builder

import leo.base.assertEqualTo
import leo15.lambda.runtime.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun id_() {
		id<Nothing>()
			.build
			.eval
			.assertEqualTo(emptyScope<Nothing>() closure lambda(at(0)))
	}
}