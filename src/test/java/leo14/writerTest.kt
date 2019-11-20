package leo14

import leo.base.assertEqualTo
import leo.base.indent
import leo13.stack
import leo14.syntax.colored
import leo14.syntax.of
import leo14.syntax.valueKeywordKind
import leo14.writer.nullWriterParent
import leo14.writer.parent
import leo14.writer.write
import leo14.writer.writer
import kotlin.test.Test

class WriterTest {
	@Test
	fun writeLiteral() {
		nullWriterParent
			.writer(
				lineStack = stack("plus: one", "plus: two"),
				nameStack = stack("one", "two"),
				column = 15,
				indent = 3.indent)
			.write(token(literal(123)) of valueKeywordKind)
			.assertEqualTo(
				nullWriterParent
					.writer(
						lineStack = stack("plus: one", "plus: two"),
						nameStack = stack("one", "two", literal(123).toString() colored valueKeywordKind),
						column = 15,
						indent = 3.indent))
	}

	@Test
	fun writeBegin() {
		val writer = nullWriterParent
			.writer(
				lineStack = stack("plus: one", "plus: two"),
				nameStack = stack("one", "two"),
				column = 15,
				indent = 3.indent)
		writer
			.write(token(begin("foo")) of valueKeywordKind)
			.assertEqualTo(
				writer
					.parent("foo" colored valueKeywordKind)
					.writer(
						lineStack = stack(),
						nameStack = stack(),
						column = 20,
						indent = 3.indent))
	}

	@Test
	fun writeEnd_empty() {
		nullWriterParent
			.writer(
				lineStack = stack("plus: one", "plus: two"),
				nameStack = stack("one", "two"),
				column = 15,
				indent = 3.indent)
			.parent("foo")
			.writer(
				lineStack = stack(),
				nameStack = stack(),
				column = 20,
				indent = 3.indent)
			.write(token(end) of valueKeywordKind)
			.assertEqualTo(
				nullWriterParent
					.writer(
						lineStack = stack("plus: one", "plus: two"),
						nameStack = stack("one", "two", "foo"),
						column = 15,
						indent = 3.indent))
	}

	@Test
	fun writeEnd_names() {
		nullWriterParent
			.writer(
				lineStack = stack("plus: one", "plus: two"),
				nameStack = stack("one", "two"),
				column = 15,
				indent = 3.indent)
			.parent("foo")
			.writer(
				lineStack = stack(),
				nameStack = stack("three", "four"),
				column = 20,
				indent = 3.indent)
			.write(token(end) of valueKeywordKind)
			.assertEqualTo(
				nullWriterParent
					.writer(
						lineStack = stack("plus: one", "plus: two", "one two", "foo: three four"),
						nameStack = stack(),
						column = 15,
						indent = 3.indent))
	}

	@Test
	fun writeEnd_lines() {
		nullWriterParent
			.writer(
				lineStack = stack("plus: one", "plus: two"),
				nameStack = stack("one", "two"),
				column = 15,
				indent = 3.indent)
			.parent("foo")
			.writer(
				lineStack = stack("plus: three", "plus: four"),
				nameStack = stack("three", "four"),
				column = 20,
				indent = 3.indent)
			.write(token(end) of valueKeywordKind)
			.assertEqualTo(
				nullWriterParent
					.writer(
						lineStack = stack(
							"plus: one",
							"plus: two",
							"one two",
							"foo",
							"  plus: three",
							"  plus: four",
							"  three four"),
						nameStack = stack(),
						column = 15,
						indent = 3.indent))
	}
}