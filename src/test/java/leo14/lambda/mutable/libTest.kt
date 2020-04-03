package leo14.lambda.mutable

import leo.base.assertEqualTo
import kotlin.test.Test

class LibTest {
	@Test
	fun id_() {
		term(id(), term(123))
			.apply
			.assertEqualTo(term(123))
	}
}