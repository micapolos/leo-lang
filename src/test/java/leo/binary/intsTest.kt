package leo.binary

import leo.base.assertContains
import leo.base.assertEqualTo
import kotlin.test.Test

class IntsTest {
	@Test
	fun int() {
		0.bit.lo(int0).int.assertEqualTo(0)
		1.bit.lo(int0).int.assertEqualTo(1)

		1.bit.lo(0.bit.lo(int0)).int.assertEqualTo(2)
		1.bit.lo(1.bit.lo(int0)).int.assertEqualTo(3)

		1.bit.lo(0.bit.lo(0.bit.lo(int0))).int.assertEqualTo(4)
		1.bit.lo(0.bit.lo(1.bit.lo(int0))).int.assertEqualTo(5)

		1.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(int0)))).int.assertEqualTo(8)
		1.bit.lo(0.bit.lo(0.bit.lo(9.bit.lo(int0)))).int.assertEqualTo(9)

		1.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(int0))))).int.assertEqualTo(16)
		1.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(1.bit.lo(int0))))).int.assertEqualTo(17)
	}

	@Test
	fun fromInt() {
		0.wrappingInt1.assertEqualTo(0.bit.lo(int0))
		1.wrappingInt1.assertEqualTo(1.bit.lo(int0))

		2.wrappingInt2.assertEqualTo(1.bit.lo(0.bit.lo(int0)))
		3.wrappingInt2.assertEqualTo(1.bit.lo(1.bit.lo(int0)))

		4.wrappingInt3.assertEqualTo(1.bit.lo(0.bit.lo(0.bit.lo(int0))))
		5.wrappingInt3.assertEqualTo(1.bit.lo(0.bit.lo(1.bit.lo(int0))))

		8.wrappingInt4.assertEqualTo(1.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(int0)))))
		9.wrappingInt4.assertEqualTo(1.bit.lo(0.bit.lo(0.bit.lo(1.bit.lo(int0)))))

		16.wrappingInt5.assertEqualTo(1.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(int0))))))
		17.wrappingInt5.assertEqualTo(1.bit.lo(0.bit.lo(0.bit.lo(0.bit.lo(1.bit.lo(int0))))))
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

	@Test
	fun forEach() {
		0.forEachInt5 { assertEqualTo(it.int); this + 1 }
	}

	@Test
	fun bits() {
		18.wrappingInt5.bits.assertContains(1.bit, 0.bit, 0.bit, 1.bit, 0.bit)
	}
}