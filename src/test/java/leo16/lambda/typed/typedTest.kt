package leo16.lambda.typed

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import leo16.lambda.type.emptyType
import leo16.lambda.typed.of
import leo16.lambda.typed.typed
import kotlin.test.Test

class TypedTest {
	@Test
	fun empty() {
		typed().assertEqualTo(idTerm of emptyType)
	}
}