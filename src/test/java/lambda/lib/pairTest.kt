package lambda.lib

import lambda.invoke
import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import kotlin.test.Test

class PairTest {
	@Test
	fun equality() {
		pair(zero, zero).assertEqualTo(pair(zero, zero))
		pair(zero, zero).assertNotEqualTo(pair(zero, one))
		pair(zero, zero).assertNotEqualTo(pair(one, zero))
		pair(zero, zero).assertNotEqualTo(pair(one, one))

		pair(zero, one).assertNotEqualTo(pair(zero, zero))
		pair(zero, one).assertEqualTo(pair(zero, one))
		pair(zero, one).assertNotEqualTo(pair(one, zero))
		pair(zero, one).assertNotEqualTo(pair(one, one))

		pair(one, zero).assertNotEqualTo(pair(zero, zero))
		pair(one, zero).assertNotEqualTo(pair(zero, one))
		pair(one, zero).assertEqualTo(pair(one, zero))
		pair(one, zero).assertNotEqualTo(pair(one, one))

		pair(one, one).assertNotEqualTo(pair(zero, zero))
		pair(one, one).assertNotEqualTo(pair(zero, one))
		pair(one, one).assertNotEqualTo(pair(one, zero))
		pair(one, one).assertEqualTo(pair(one, one))
	}

	@Test
	fun math() {
		pair(zero, zero).at(zero).assertEqualTo(zero)
		pair(zero, zero).at(one).assertEqualTo(zero)

		pair(zero, one).at(zero).assertEqualTo(zero)
		pair(zero, one).at(one).assertEqualTo(one)

		pair(one, zero).at(zero).assertEqualTo(one)
		pair(one, zero).at(one).assertEqualTo(zero)

		pair(one, one).at(zero).assertEqualTo(one)
		pair(one, one).at(one).assertEqualTo(one)
	}
}