package leo14.writer

import leo.base.assertEqualTo
import leo.base.indent
import leo14.literal
import leo14.syntax.*
import leo14.token
import kotlin.test.Test

class WriterTest {
	@Test
	fun writeLiteral() {
		nullWriterParent
			.writer(
				value.code(word("plus") to value.code()),
				column = 15,
				indent = 3.indent)
			.write(token(literal(123)) of valueKeywordKind)
			.assertEqualTo(
				nullWriterParent
					.writer(
						value.code(word("plus") to value.code()),
						column = 15,
						indent = 3.indent))
	}
//
//	@Test
//	fun writeBegin() {
//		val writer = nullWriterParent
//			.writer(
//				script(valueKind, "plus" lineTo script(valueKind)),
//				column = 15,
//				indent = 3.indent)
//		writer
//			.write(token(begin("foo")) of valueKeywordKind)
//			.assertEqualTo(
//				writer
//					.parent("foo" colored valueKeywordKind)
//					.writer(
//						script(valueKind),
//						column = 20,
//						indent = 3.indent))
//	}
//
//	@Test
//	fun writeEnd_empty() {
//		nullWriterParent
//			.writer(
//				script(valueKind, "plus" lineTo script(valueKind)),
//				column = 15,
//				indent = 3.indent)
//			.parent("foo")
//			.writer(
//				lineStack = stack(),
//				nameStack = stack(),
//				column = 20,
//				indent = 3.indent)
//			.write(token(end) of valueKeywordKind)
//			.assertEqualTo(
//				nullWriterParent
//					.writer(
//						lineStack = stack("plus: one", "plus: two"),
//						nameStack = stack("one", "two", "foo"),
//						column = 15,
//						indent = 3.indent))
//	}
//
//	@Test
//	fun writeEnd_names() {
//		nullWriterParent
//			.writer(
//				lineStack = stack("plus: one", "plus: two"),
//				nameStack = stack("one", "two"),
//				column = 15,
//				indent = 3.indent)
//			.parent("foo")
//			.writer(
//				lineStack = stack(),
//				nameStack = stack("three", "four"),
//				column = 20,
//				indent = 3.indent)
//			.write(token(end) of valueKeywordKind)
//			.assertEqualTo(
//				nullWriterParent
//					.writer(
//						lineStack = stack("plus: one", "plus: two", "one two", "foo: three four"),
//						nameStack = stack(),
//						column = 15,
//						indent = 3.indent))
//	}
//
//	@Test
//	fun writeEnd_lines() {
//		nullWriterParent
//			.writer(
//				lineStack = stack("plus: one", "plus: two"),
//				nameStack = stack("one", "two"),
//				column = 15,
//				indent = 3.indent)
//			.parent("foo")
//			.writer(
//				lineStack = stack("plus: three", "plus: four"),
//				nameStack = stack("three", "four"),
//				column = 20,
//				indent = 3.indent)
//			.write(token(end) of valueKeywordKind)
//			.assertEqualTo(
//				nullWriterParent
//					.writer(
//						lineStack = stack(
//							"plus: one",
//							"plus: two",
//							"one two",
//							"foo",
//							"  plus: three",
//							"  plus: four",
//							"  three four"),
//						nameStack = stack(),
//						column = 15,
//						indent = 3.indent))
//	}
}