package leo14

import leo.base.assertEqualTo
import leo13.base.linesString
import kotlin.test.Test

class FragmentTest {
	@Test
	fun indentString_empty() {
		emptyFragment
			.indentString
			.assertEqualTo("")
	}

	@Test
	fun indentString_begin() {
		emptyFragment
			.begin("foo")
			.indentString
			.assertEqualTo(
				linesString(
					"foo",
					"  "))
	}

	@Test
	fun indentString_begin_begin() {
		emptyFragment
			.begin("foo")
			.begin("bar")
			.indentString
			.assertEqualTo(
				linesString(
					"foo",
					"  bar",
					"    "))
	}

	@Test
	fun indentString_begin_begin_end() {
		emptyFragment
			.begin("foo")
			.begin("bar")
			.end
			.indentString
			.assertEqualTo(
				linesString(
					"foo",
					"  bar",
					"  "))
	}

	@Test
	fun indentString_begin_begin_end_end() {
		emptyFragment
			.begin("foo")
			.begin("bar")
			.end
			.end
			.indentString
			.assertEqualTo(
				linesString(
					"foo: bar",
					""))
	}

	@Test
	fun indentString_begin_begin_end_begin() {
		emptyFragment
			.begin("foo")
			.begin("bar")
			.end
			.begin("zoo")
			.indentString
			.assertEqualTo(
				linesString(
					"foo",
					"  bar",
					"  zoo",
					"    "))
	}

	@Test
	fun indentString_struct() {
		emptyFragment.apply { indentString.assertEqualTo("") }
			.begin("point").apply { indentString.assertEqualTo("point\n  ") }
			.begin("x").apply { indentString.assertEqualTo("point\n  x\n    ") }
			.plus(literal(10)).apply { indentString.assertEqualTo("point\n  x\n    10\n    ") }
			.end.apply { indentString.assertEqualTo("point\n  x: 10\n  ") }
			.begin("y").apply { indentString.assertEqualTo("point\n  x: 10\n  y\n    ") }
			.plus(literal(10)).apply { indentString.assertEqualTo("point\n  x: 10\n  y\n    10\n    ") }
			.end.apply { indentString.assertEqualTo("point\n  x: 10\n  y: 10\n  ") }
			.end.apply { indentString.assertEqualTo("point\n  x: 10\n  y: 10\n") }
	}
}