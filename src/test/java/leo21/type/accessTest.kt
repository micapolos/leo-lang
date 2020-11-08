package leo21.type

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class AccessTest {
	@Test
	fun struct_matchFirst() {
		type(
			"x" lineTo doubleType,
			"y" lineTo stringType)
			.accessOrNull("x")
			.assertEqualTo(doubleType)
	}

	@Test
	fun struct_matchSecond() {
		type(
			"x" lineTo doubleType,
			"y" lineTo stringType)
			.accessOrNull("y")
			.assertEqualTo(stringType)
	}

	@Test
	fun struct_mismatch() {
		type(
			"x" lineTo doubleType,
			"y" lineTo stringType)
			.accessOrNull("z")
			.assertNull
	}

	@Test
	fun choice_matchFirst() {
		type(
			choice(
				"x" lineTo doubleType,
				"y" lineTo stringType))
			.accessOrNull("x")
			.assertEqualTo(doubleType)
	}

	@Test
	fun choice_matchSecond() {
		type(
			choice(
				"x" lineTo doubleType,
				"y" lineTo stringType))
			.accessOrNull("y")
			.assertEqualTo(stringType)
	}

	@Test
	fun choice_mismatch() {
		type(
			choice(
				"x" lineTo doubleType,
				"y" lineTo stringType))
			.accessOrNull("z")
			.assertNull
	}

	@Test
	fun recursive_match() {
		type(
			recursive(
				type(
					"list" lineTo type(
						"empty" lineTo type(),
						"tail" lineTo type(recurse(0))))))
			.accessOrNull("list")
			.assertEqualTo(
				type(
					recursive(
						type(
							"empty" lineTo type(),
							"tail" lineTo type(
								"list" lineTo type(recurse(0)))))))
	}

	@Test
	fun recursive_mismatch() {
		type(
			recursive(
				type(
					"list" lineTo type(
						"empty" lineTo type(),
						"tail" lineTo type(recurse(0))))))
			.accessOrNull("foo")
			.assertNull
	}

	@Test
	fun recurse() {
		type(recurse(0))
			.accessOrNull("list")
			.assertNull
	}
}