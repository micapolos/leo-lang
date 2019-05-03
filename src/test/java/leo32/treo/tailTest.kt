package leo32.treo

import leo.base.assertEqualTo
import leo.binary.bit0
import leo.binary.bit1
import kotlin.test.Test

class TailTest {
	@Test
	fun tail() {
		tail().assertEqualTo(tail(null))
		tail(value(constant(bit0)), value(constant(bit1)))
			.assertEqualTo(
				tail(chain(
					head(value(constant(bit0))),
					tail(chain(
						head(value(constant(bit1))),
						tail(null))))))
	}
}