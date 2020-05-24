package leo16.lambda

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import kotlin.test.Test

class TypedTest {
	@Test
	fun empty() {
		typed().assertEqualTo(idTerm of emptyType)
	}
}