package leo.base

import kotlin.test.Test

class OptionalTest {
	@Test
	fun ifPresent() {
		12.present
			.ifPresent { toString() }
			.assertEqualTo("12".present)

		absent<Int>()
			.ifPresent { toString() }
			.assertEqualTo(absent())
	}

	@Test
	fun ifAbsent() {
		12.present
			.ifAbsent { 15 }
			.assertEqualTo(12)

		absent<Int>()
			.ifAbsent { 15 }
			.assertEqualTo(15)
	}

	@Test
	fun ifPresentOptional() {
		12.present
			.ifPresentOptional { "15".present }
			.assertEqualTo("15".present)

		12.present
			.ifPresentOptional { absent<String>() }
			.assertEqualTo(absent())

		absent<Int>()
			.ifPresentOptional { "15".present }
			.assertEqualTo(absent())

		absent<Int>()
			.ifPresentOptional { absent<String>() }
			.assertEqualTo(absent())
	}
}