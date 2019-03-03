package leo.binary

import leo.base.assertEqualTo
import kotlin.test.Test

class IntsTest {
	@Test
	fun int() {
		int0.push(0.bit).int.assertEqualTo(0)
		int0.push(1.bit).int.assertEqualTo(1)

		int0.push(1.bit).push(0.bit).int.assertEqualTo(2)
		int0.push(1.bit).push(1.bit).int.assertEqualTo(3)

		int0.push(1.bit).push(0.bit).push(0.bit).int.assertEqualTo(4)
		int0.push(1.bit).push(0.bit).push(1.bit).int.assertEqualTo(5)

		int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).int.assertEqualTo(8)
		int0.push(1.bit).push(0.bit).push(0.bit).push(1.bit).int.assertEqualTo(9)

		int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(0.bit).int.assertEqualTo(16)
		int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(1.bit).int.assertEqualTo(17)
	}

	@Test
	fun fromInt() {
		0.wrappingInt1.assertEqualTo(int0.push(0.bit))
		1.wrappingInt1.assertEqualTo(int0.push(1.bit))

		2.wrappingInt2.assertEqualTo(int0.push(1.bit).push(0.bit))
		3.wrappingInt2.assertEqualTo(int0.push(1.bit).push(1.bit))

		4.wrappingInt3.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit))
		5.wrappingInt3.assertEqualTo(int0.push(1.bit).push(0.bit).push(1.bit))

		8.wrappingInt4.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit))
		9.wrappingInt4.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(1.bit))

		16.wrappingInt5.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(0.bit))
		17.wrappingInt5.assertEqualTo(int0.push(1.bit).push(0.bit).push(0.bit).push(0.bit).push(1.bit))
	}

	@Test
	fun inc() {
		0.wrappingInt1.incOrNull.assertEqualTo(1.wrappingInt1)
		1.wrappingInt1.incOrNull.assertEqualTo(null)

		0.wrappingInt5.incOrNull.assertEqualTo(1.wrappingInt5)
		1.wrappingInt5.incOrNull.assertEqualTo(2.wrappingInt5)
		3.wrappingInt5.incOrNull.assertEqualTo(4.wrappingInt5)
		7.wrappingInt5.incOrNull.assertEqualTo(8.wrappingInt5)
		15.wrappingInt5.incOrNull.assertEqualTo(16.wrappingInt5)
		31.wrappingInt5.incOrNull.assertEqualTo(null)
	}
}