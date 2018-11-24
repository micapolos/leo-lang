package leo.base

import kotlin.test.Test

class StackTest {
	@Test
	fun all_hasNone() {
		stack(1, 2, 3, 4)
			.all { false }
			.assertEqualTo(null)
	}

	@Test
	fun all_hasOne() {
		stack(1, 2, 3, 4)
			.all { it == 2 }
			.assertEqualTo(stack(2))
	}

	@Test
	fun all_hasMany() {
		stack(1, 2, 3, 4)
			.all { it % 2 == 0 }
			.assertEqualTo(stack(2, 4))
	}

	@Test
	fun only_hasOne() {
		stack(1).onlyOrNull.assertEqualTo(1)
	}

	@Test
	fun only_hasMany() {
		stack(1, 2).onlyOrNull.assertEqualTo(null)
	}

	@Test
	fun fold_null() {
		"".fold(nullOf<Stack<Int>>(), String::plus)
			.assertEqualTo("")
	}

	@Test
	fun fold_nonNull() {
		"".fold(stack(1, 2, 3), String::plus)
			.assertEqualTo("321")
	}

	@Test
	fun map() {
		stack(1, 2, 3)
			.map(Int::toString)
			.assertEqualTo(stack("1", "2", "3"))
	}

	@Test
	fun mapOrNull_notNull() {
		stack(1, 2, 3)
			.mapOrNull(Int::toString)
			.assertEqualTo(stack("1", "2", "3"))
	}


	@Test
	fun mapOrNull_null() {
		stack(1, 2, 3)
			.mapOrNull { int ->
				if (int == 3) null else int.toString()
			}
			.assertEqualTo(null)
	}
}