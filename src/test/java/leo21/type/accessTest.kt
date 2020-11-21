package leo21.type

import leo.base.assertEqualTo
import leo.base.assertNull
import kotlin.test.Test

class AccessTest {
	@Test
	fun struct_matchFirst() {
		type(
			"x" lineTo numberType,
			"y" lineTo stringType)
			.accessOrNull("x")
			.assertEqualTo(numberType)
	}

	@Test
	fun struct_matchSecond() {
		type(
			"x" lineTo numberType,
			"y" lineTo stringType)
			.accessOrNull("y")
			.assertEqualTo(stringType)
	}

	@Test
	fun struct_mismatch() {
		type(
			"x" lineTo numberType,
			"y" lineTo stringType)
			.accessOrNull("z")
			.assertNull
	}

	@Test
	fun choice_matchFirst() {
		type(
			line(
				choice(
					"x" lineTo numberType,
					"y" lineTo stringType)))
			.accessOrNull("x")
			.assertEqualTo(numberType)
	}

	@Test
	fun choice_matchSecond() {
		type(
			line(
				choice(
					"x" lineTo numberType,
					"y" lineTo stringType)))
			.accessOrNull("y")
			.assertEqualTo(stringType)
	}

	@Test
	fun choice_mismatch() {
		type(
			line(
				choice(
					"x" lineTo numberType,
					"y" lineTo stringType)))
			.accessOrNull("z")
			.assertNull
	}

	@Test
	fun recursive_match() {
		type(
			line(
				recursive(
					"list" lineTo type(
						"empty" lineTo type(),
						"tail" lineTo type(line(recurse(0)))))))
			.accessOrNull("list")
			.assertEqualTo(
				type(
					line(recursive("empty" lineTo type())),
					line(recursive("tail" lineTo type(
						"list" lineTo type(line(recurse(0))))))))
	}

	@Test
	fun recursive_mismatch() {
		type(
			line(
				recursive(
					"list" lineTo type(
						"empty" lineTo type(),
						"tail" lineTo type(line(recurse(0)))))))
			.accessOrNull("foo")
			.assertNull
	}

	@Test
	fun recurse() {
		type(line(recurse(0)))
			.accessOrNull("list")
			.assertNull
	}
}