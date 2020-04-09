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

	@Test
	fun listPlusTypeMismatch() {
		leo(
			listName(ofName(numberName())),
			plusName("foo"))
			.assertEvalsTo(leo(listName(), plusName("foo")))

		leo(
			listName(ofName(numberName())),
			plusName(10),
			plusName(20),
			plusName("foo"))
			.assertEvalsTo(leo(listName(10, 20), plusName("foo")))
	}

	// TODO: fixit!!!
//	@Test
//	fun listPlusStatic() {
//		leo(listName(ofName("foo"())), plusName("foo"()))
//			.assertEvalsTo(leo(listName("foo"())))
//	}

	@Test
	fun javaNull() {
		leo(javaName(nullName()))
			.assertEvalsTo(leo(nativeName(nullValue.nativeString)))
	}

	@Test
	fun listJavaArray() {
		leo(
			listName(ofName(numberName())),
			javaName(), arrayName())
			.assertEvalsTo(leo(arrayName(nativeName(arrayOf<Value>().nativeString))))

		leo(
			listName(ofName(numberName())),
			plusName(1),
			plusName(2),
			plusName(3),
			javaName(), arrayName())
			.assertEvalsTo(leo(arrayName(nativeName(arrayOf(1, 2, 3).nativeString))))
	}

	@Test
	fun stringJavaClass() {
		leo("java.lang.String", javaName(), className())
			.assertEvalsTo(leo(className(nativeName(java.lang.String::class.java.toString()))))
	}

	@Test
	fun nativeClassField() {
		leo(
			"java.lang.Integer", javaName(), className(),
			fieldName(nameName("MAX_VALUE")))
			.assertEvalsTo(leo(
				fieldName(
					nativeName(
						java.lang.Integer::class.java.getField("MAX_VALUE").nativeString))))
	}

	@Test
	fun nativeClassConstructor() {
		leo("java.lang.StringBuilder", javaName(), className(), constructorName())
			.assertEvalsTo(leo(
				constructorName(
					nativeName(
						java.lang.StringBuilder::class.java.getConstructor().nativeString))))
	}

	@Test
	fun nativeClassConstructorParameterList() {
		leo(
			"java.lang.StringBuilder", javaName(), className(),
			constructorName(parameterName(
				listName(ofName(nativeName(), className())),
				plusName("java.lang.String", javaName(), className()))))
			.assertEvalsTo(leo(
				constructorName(
					nativeName(
						java.lang.StringBuilder::class.java.getConstructor(String::class.java).nativeString))))
	}

	@Test
	fun nativeClassConstructorParameterListPrimitiveTypes() {
		leo("java.awt.Point", javaName(), className(),
			constructorName(
				parameterName(
					listName(ofName(className(nativeName()))),
					plusName(
						"java.lang.Integer", javaName(), className(),
						fieldName(nameName("TYPE")),
						getName(javaName(nullName())),
						className()),
					plusName(
						"java.lang.Integer", javaName(), className(),
						fieldName(nameName("TYPE")),
						getName(javaName(nullName())),
						className()))))
			.assertEvalsTo(leo(
				constructorName(
					nativeName(
						java.awt.Point::class
							.java
							.getConstructor(Integer.TYPE, Integer.TYPE)
							.nativeString))))
	}

	@Test
	fun nativeClassMethod() {
		leo("java.lang.String", javaName(), className(),
			methodName(nameName("length")))
			.assertEvalsTo(leo(
				methodName(
					nativeName(
						java.lang.String::class.java.getMethod("length").nativeString))))
	}

	@Test
	fun nativeFieldGetStatic() {
		leo("java.lang.Integer", javaName(), className(),
			fieldName(nameName("MAX_VALUE")),
			getName(javaName(nullName())))
			.assertEvalsTo(leo(
				nativeName(
					java.lang.Integer::class
						.java
						.getField("MAX_VALUE")
						.get(null)
						.nativeString)))
	}
}