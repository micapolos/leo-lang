package lambda.lib

import lambda.invoke
import leo.base.assertEqualTo
import leo.base.assertNotEqualTo
import kotlin.test.Test

class PairTest {
	@Test
	fun equality() {
		pair(zeroBit, zeroBit).assertEqualTo(pair(zeroBit, zeroBit))
		pair(zeroBit, zeroBit).assertNotEqualTo(pair(zeroBit, oneBit))
		pair(zeroBit, zeroBit).assertNotEqualTo(pair(oneBit, zeroBit))
		pair(zeroBit, zeroBit).assertNotEqualTo(pair(oneBit, oneBit))

		pair(zeroBit, oneBit).assertNotEqualTo(pair(zeroBit, zeroBit))
		pair(zeroBit, oneBit).assertEqualTo(pair(zeroBit, oneBit))
		pair(zeroBit, oneBit).assertNotEqualTo(pair(oneBit, zeroBit))
		pair(zeroBit, oneBit).assertNotEqualTo(pair(oneBit, oneBit))

		pair(oneBit, zeroBit).assertNotEqualTo(pair(zeroBit, zeroBit))
		pair(oneBit, zeroBit).assertNotEqualTo(pair(zeroBit, oneBit))
		pair(oneBit, zeroBit).assertEqualTo(pair(oneBit, zeroBit))
		pair(oneBit, zeroBit).assertNotEqualTo(pair(oneBit, oneBit))

		pair(oneBit, oneBit).assertNotEqualTo(pair(zeroBit, zeroBit))
		pair(oneBit, oneBit).assertNotEqualTo(pair(zeroBit, oneBit))
		pair(oneBit, oneBit).assertNotEqualTo(pair(oneBit, zeroBit))
		pair(oneBit, oneBit).assertEqualTo(pair(oneBit, oneBit))
	}

	@Test
	fun math() {
		pair(zeroBit, zeroBit).pairAt(zeroBit).assertEqualTo(zeroBit)
		pair(zeroBit, zeroBit).pairAt(oneBit).assertEqualTo(zeroBit)

		pair(zeroBit, oneBit).pairAt(zeroBit).assertEqualTo(zeroBit)
		pair(zeroBit, oneBit).pairAt(oneBit).assertEqualTo(oneBit)

		pair(oneBit, zeroBit).pairAt(zeroBit).assertEqualTo(oneBit)
		pair(oneBit, zeroBit).pairAt(oneBit).assertEqualTo(zeroBit)

		pair(oneBit, oneBit).pairAt(zeroBit).assertEqualTo(oneBit)
		pair(oneBit, oneBit).pairAt(oneBit).assertEqualTo(oneBit)
	}
}