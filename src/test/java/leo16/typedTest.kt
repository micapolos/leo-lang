package leo16

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import leo15.plus
import leo16.names.*
import kotlin.test.Test

class TypedTest {
	@Test
	fun of() {
		value(_zero())
			.of(value(_zero()).type)
			.assertEqualTo(idTerm.of(value(_zero()).type))
	}
}