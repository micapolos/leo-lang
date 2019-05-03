package leo32.treo

import leo.base.assertEqualTo
import leo.binary.bit0
import leo.binary.bit1
import org.junit.Test

class ChainTest {
	@Test
	fun chainVararg() {
		chain(value(constant(bit0)))
			.assertEqualTo(chain(head(value(constant(bit0))), tail(null)))
		chain(value(constant(bit0)), value(constant(bit1)))
			.assertEqualTo(chain(
				head(value(constant(bit0))),
				tail(chain(
					head(value(constant(bit1))),
					tail(null)))))
	}
}