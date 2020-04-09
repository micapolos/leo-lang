package leo14.untyped.typed

import leo14.invoke
import leo14.leo
import leo14.untyped.*
import kotlin.test.Test

class EvalTest {
	@Test
	fun normalization() {
		leo("foo"(), "bar"()).assertEvalsTo(leo("bar"("foo"())))
	}

	@Test
	fun literals() {
		leo(2).assertEvalsTo(leo(2))
		leo("foo").assertEvalsTo(leo("foo"))
		leo(2, 3).assertEvalsTo(leo(2, 3))
		leo("foo", "bar").assertEvalsTo(leo("foo", "bar"))
	}

	@Test
	fun primitives() {
		// TODO: These should be defined in a library, using reflection
		leo(minusName(2)).assertEvalsTo(leo(-2))
		leo(2, minusName()).assertEvalsTo(leo(-2))
		leo(2, plusName(3)).assertEvalsTo(leo(5))
		leo(5, minusName(3)).assertEvalsTo(leo(2))
		leo(2, timesName(3)).assertEvalsTo(leo(6))
	}

	@Test
	fun listOf() {
		leo(listName(ofName(numberName())))
			.assertEvalsTo(leo(listName()))
	}

	@Test
	fun listPlusDynamic() {
		leo(
			listName(ofName(numberName())),
			plusName(123))
			.assertEvalsTo(leo(listName(123)))

		leo(
			listName(ofName(numberName())),
			plusName(123),
			plusName(124),
			plusName(125))
			.assertEvalsTo(leo(listName(123, 124, 125)))
	}

	// TODO: fixit!!!
//	@Test
//	fun listPlusStatic() {
//		leo(listName(ofName("foo"())), plusName("foo"()))
//			.assertEvalsTo(leo(listName("foo"())))
//	}

	@Test
	fun stringJavaClass() {
		leo("java.lang.String", javaName(), className())
			.assertEvalsTo(leo(className(nativeName(java.lang.String::class.java.toString()))))
	}
}