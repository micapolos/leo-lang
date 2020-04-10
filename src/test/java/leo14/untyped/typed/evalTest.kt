package leo14.untyped.typed

import leo14.invoke
import leo14.leo
import leo14.untyped.*
import java.net.URL
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
	fun get() {
		leo("point"("x"(10), "y"(20)), "x"()).assertEvalsTo(leo("x"(10)))
		leo("point"("x"(10), "y"(20)), "y"()).assertEvalsTo(leo("y"(20)))
		leo("point"("x"(10), "y"(20)), "z"()).assertEvalsTo(leo("z"("point"("x"(10), "y"(20)))))
	}

	@Test
	fun number() {
		leo(numberName()).assertEvalsTo(leo(numberName()))
	}

	@Test
	fun nativeAccess() {
		leo(nativeName(className(nameName("java.lang.String"))), nativeName())
			.assertEvalsTo(leo(nativeName(java.lang.String::class.java.nativeString)))
	}

	@Test
	fun text() {
		leo(textName()).assertEvalsTo(leo(textName()))
	}

	@Test
	fun listOf() {
		leo(listName(ofName(numberName())))
			.assertEvalsTo(leo(listName()))
	}

//	@Test
//	fun listPlusDynamic() {
//		leo(
//			listName(ofName(numberName())),
//			plusName(123))
//			.assertEvalsTo(leo(listName(123)))
//
//		leo(
//			listName(ofName(numberName())),
//			plusName(123),
//			plusName(124),
//			plusName(125))
//			.assertEvalsTo(leo(listName(123, 124, 125)))
//	}
//
//	@Test
//	fun listPlusTypeMismatch() {
//		leo(
//			listName(ofName(numberName())),
//			plusName("foo"))
//			.assertEvalsTo(leo(listName(), plusName("foo")))
//
//		leo(
//			listName(ofName(numberName())),
//			plusName(10),
//			plusName(20),
//			plusName("foo"))
//			.assertEvalsTo(leo(listName(10, 20), plusName("foo")))
//	}

	// TODO: fixit!!!
//	@Test
//	fun listPlusStatic() {
//		leo(listName(ofName("foo"())), plusName("foo"()))
//			.assertEvalsTo(leo(listName("foo"())))
//	}

	@Test
	fun javaNull() {
		leo(nativeName(nullName()))
			.assertEvalsTo(leo(nativeName(nullValue.nativeString)))
	}

//	@Test
//	fun listJavaArray() {
//		leo(
//			listName(ofName(numberName())),
//			javaName(), arrayName())
//			.assertEvalsTo(leo(arrayName(nativeName(arrayOf<Value>().nativeString))))
//
//		leo(
//			listName(ofName(numberName())),
//			plusName(1),
//			plusName(2),
//			plusName(3),
//			javaName(), arrayName())
//			.assertEvalsTo(leo(arrayName(nativeName(arrayOf(1, 2, 3).nativeString))))
//	}

	@Test
	fun nativeClassNameText() {
		leo(nativeName(className(nameName("java.lang.Integer"))))
			.assertEvalsTo(leo(
				className(
					nativeName(
						java.lang.Integer::class.java.nativeString))))
	}

	@Test
	fun nativeClassField() {
		leo(
			nativeName(className(nameName("java.lang.Integer"))),
			fieldName(nameName("MAX_VALUE")))
			.assertEvalsTo(leo(
				fieldName(
					nativeName(
						java.lang.Integer::class.java.getField("MAX_VALUE").nativeString))))
	}

	@Test
	fun nativeClassConstructor() {
		leo(
			nativeName(className(nameName("java.lang.StringBuilder"))),
			constructorName())
			.assertEvalsTo(leo(
				constructorName(
					nativeName(
						java.lang.StringBuilder::class.java.getConstructor().nativeString))))
	}

	@Test
	fun nativeClassConstructorParameterList() {
		leo(
			nativeName(className(nameName("java.lang.StringBuilder"))),
			constructorName(parameterName(
				listName(ofName(className(nativeName()))),
				plusName(nativeName(className(nameName("java.lang.String")))))))
			.assertEvalsTo(leo(
				constructorName(
					nativeName(
						java.lang.StringBuilder::class.java.getConstructor(String::class.java).nativeString))))
	}

	@Test
	fun nativeClassConstructorParameterListPrimitiveTypes() {
		leo(
			nativeName(className(nameName("java.awt.Point"))),
			constructorName(
				parameterName(
					listName(ofName(className(nativeName()))),
					plusName(
						nativeName(className(nameName("java.lang.Integer"))),
						fieldName(nameName("TYPE")),
						getName(nativeName(nullName())),
						className()),
					plusName(
						nativeName(className(nameName("java.lang.Integer"))),
						fieldName(nameName("TYPE")),
						getName(nativeName(nullName())),
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
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(nameName("length")))
			.assertEvalsTo(leo(
				methodName(
					nativeName(
						java.lang.String::class.java.getMethod("length").nativeString))))
	}

	@Test
	fun nativeClassMethodNameParameterList() {
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(
				nameName("substring"),
				parameterName(
					listName(ofName(className(nativeName()))),
					plusName(className(
						nativeName(className(nameName("java.lang.Integer"))),
						fieldName(nameName("TYPE")),
						getName(nativeName(nullName())))),
					plusName(className(
						nativeName(className(nameName("java.lang.Integer"))),
						fieldName(nameName("TYPE")),
						getName(nativeName(nullName())))))))
			.assertEvalsTo(
				leo(
					methodName(
						nativeName(java.lang.String::class
							.java
							.getMethod("substring", Integer.TYPE, Integer.TYPE)
							.nativeString))))
	}

	@Test
	fun nativeFieldGetStatic() {
		leo(
			nativeName(className(nameName("java.lang.Integer"))),
			fieldName(nameName("MAX_VALUE")),
			getName(nativeName(nullName())))
			.assertEvalsTo(leo(
				nativeName(
					java.lang.Integer::class
						.java
						.getField("MAX_VALUE")
						.get(null)
						.nativeString)))
	}

	@Test
	fun nativeConstructorInvoke() {
		leo(
			nativeName(className(nameName("java.lang.StringBuilder"))),
			constructorName(),
			invokeName())
			.assertEvalsTo(leo(nativeName(StringBuilder().nativeString)))
	}

	@Test
	fun nativeConstructorInvokeParameterList() {
		leo(
			nativeName(className(nameName("java.net.URL"))),
			constructorName(parameterName(
				listName(ofName(className(nativeName()))),
				plusName(nativeName(className(nameName("java.lang.String")))),
				plusName(nativeName(className(nameName("java.lang.String")))),
				plusName(nativeName(className(nameName("java.lang.String")))))),
			invokeName(parameterName(
				listName(ofName(nativeName())),
				plusName(nativeName("http")),
				plusName(nativeName("www.google.com")),
				plusName(nativeName("/search")))))
			.assertEvalsTo(leo(nativeName(URL("http", "www.google.com", "/search").nativeString)))
	}

	@Test
	fun nativeMethodInvoke() {
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(nameName("length")),
			invokeName(nativeName("Hello, world!")))
			.assertEvalsTo(leo(nativeName(13.nativeString)))
	}

	@Test
	fun nativeMethodInvokeParameterList() {
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(
				nameName("replaceAll"),
				parameterName(
					listName(ofName(className(nativeName()))),
					plusName(nativeName(className(nameName("java.lang.String")))),
					plusName(nativeName(className(nameName("java.lang.String")))))),
			invokeName(
				objectName(nativeName("Hello, world!")),
				parameterName(
					listName(ofName(nativeName())),
					plusName(nativeName("world")),
					plusName(nativeName("universe")))),
			textName())
			.assertEvalsTo(leo("Hello, universe!"))
	}
}