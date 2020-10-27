package leo20

import leo.base.assertEqualTo
import kotlin.test.Test

class BindingTest {
	@Test
	fun numberPlusBinding() {
		numberPlusBinding
			.resolveOrNull(
				value(
					line(2),
					"plus" lineTo value(line(3))))
			.assertEqualTo(value(line(5)))
	}
}