package leo16.lambda

import leo.base.assertEqualTo
import leo15.lambda.idTerm
import leo16.names.*
import kotlin.test.Test

class TypedTest {
	@Test
	fun empty() {
		typed().assertEqualTo(idTerm of emptyType)
	}

	@Test
	fun struct() {
		typed(
			_x(_zero(typed())),
			_y(_one(typed())))
			.assertEqualTo(idTerm of type(_x(_zero(type()), _y(_one(type())))))
	}
}