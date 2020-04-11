package leo14.untyped.typed

import leo.base.assertEqualTo
import leo.base.assertNull
import leo.base.iterate
import org.junit.Test

class TypeBitCountTest {
	@Test
	fun empty() {
		emptyType.bitCountOrNull.assertEqualTo(0)
	}

	@Test
	fun field() {
		type("foo").bitCountOrNull.assertEqualTo(0)
	}

	@Test
	fun alternativeCount() {
		type("foo").alternativeCount.assertEqualTo(1)
		type("foo").or(type("bar")).alternativeCount.assertEqualTo(2)
		type("foo").or(type("bar")).or(type("zar")).alternativeCount.assertEqualTo(3)
		type("foo").or(type("bar").or(type("zar"))).alternativeCount.assertEqualTo(2)
	}

	@Test
	fun alternative() {
		type("foo").iterate(1) { or(type("bar")) }.bitCountOrNull.assertEqualTo(1)
		type("foo").iterate(2) { or(type("bar")) }.bitCountOrNull.assertEqualTo(2)
		type("foo").iterate(3) { or(type("bar")) }.bitCountOrNull.assertEqualTo(2)
		type("foo").iterate(4) { or(type("bar")) }.bitCountOrNull.assertEqualTo(3)
		type("foo").iterate(7) { or(type("bar")) }.bitCountOrNull.assertEqualTo(3)
		type("foo").iterate(8) { or(type("bar")) }.bitCountOrNull.assertEqualTo(4)

		byteType.or(byteType).bitCountOrNull.assertEqualTo(9)
		byteType.or(intType).bitCountOrNull.assertEqualTo(33)
		intType.or(byteType).bitCountOrNull.assertEqualTo(33)
		intType.or(intType).bitCountOrNull.assertEqualTo(33)
		intType.or(nativeType).bitCountOrNull.assertNull
	}

	@Test
	fun native() {
		nativeType.bitCountOrNull.assertNull
	}

	@Test
	fun primitives() {
		bitType.bitCountOrNull.assertEqualTo(1)
		byteType.bitCountOrNull.assertEqualTo(8)
		intType.bitCountOrNull.assertEqualTo(32)
		byteArrayType.bitCountOrNull.assertNull
		stringType.bitCountOrNull.assertNull
	}
}