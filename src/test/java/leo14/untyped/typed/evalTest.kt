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
	fun is_() {
		leo("x"(), isName(123)).assertEvalsTo(leo())
	}

	@Test
	fun is_get() {
		leo("x"(), isName(123), "x"()).assertEvalsTo(leo(123))
	}

	@Test
	fun number() {
		leo(numberName()).assertEvalsTo(leo(numberName()))
	}

	@Test
	fun nativeAccess() {
		leo(nativeName(className(nameName("java.lang.String"))), nativeName())
			.assertEvalsTo(leo(java.lang.String::class.java.javaScriptLine))
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
			.assertEvalsTo(leo(nullValue.javaScriptLine))
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
	fun nativeClassPrimitive() {
		leo(nativeName(className("boolean"())))
			.assertEvalsTo(leo(className(java.lang.Boolean.TYPE.javaScriptLine)))
		leo(nativeName(className("byte"())))
			.assertEvalsTo(leo(className(java.lang.Byte.TYPE.javaScriptLine)))
		leo(nativeName(className("short"())))
			.assertEvalsTo(leo(className(java.lang.Short.TYPE.javaScriptLine)))
		leo(nativeName(className("int"())))
			.assertEvalsTo(leo(className(Integer.TYPE.javaScriptLine)))
		leo(nativeName(className("long"())))
			.assertEvalsTo(leo(className(java.lang.Long.TYPE.javaScriptLine)))
		leo(nativeName(className("float"())))
			.assertEvalsTo(leo(className(java.lang.Float.TYPE.javaScriptLine)))
		leo(nativeName(className("double"())))
			.assertEvalsTo(leo(className(java.lang.Double.TYPE.javaScriptLine)))
	}

	@Test
	fun nativeClassNameText() {
		leo(nativeName(className(nameName("java.lang.Integer"))))
			.assertEvalsTo(leo(
				className(
					java.lang.Integer::class.java.javaScriptLine)))
	}

	@Test
	fun nativeClassField() {
		leo(
			nativeName(className(nameName("java.lang.Integer"))),
			fieldName(nameName("MAX_VALUE")))
			.assertEvalsTo(leo(
				fieldName(
					java.lang.Integer::class.java.getField("MAX_VALUE").javaScriptLine)))
	}

	@Test
	fun nativeClassConstructor() {
		leo(
			nativeName(className(nameName("java.lang.StringBuilder"))),
			constructorName())
			.assertEvalsTo(leo(
				constructorName(
					java.lang.StringBuilder::class.java.getConstructor().javaScriptLine)))
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
					java.lang.StringBuilder::class.java.getConstructor(String::class.java).javaScriptLine)))
	}

	@Test
	fun nativeClassConstructorParameterListPrimitiveTypes() {
		leo(
			nativeName(className(nameName("java.awt.Point"))),
			constructorName(
				parameterName(
					listName(ofName(className(nativeName()))),
					plusName(nativeName(className("int"()))),
					plusName(nativeName(className("int"()))))))
			.assertEvalsTo(leo(
				constructorName(
					java.awt.Point::class
						.java
						.getConstructor(Integer.TYPE, Integer.TYPE)
						.javaScriptLine)))
	}

	@Test
	fun nativeClassMethod() {
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(nameName("length")))
			.assertEvalsTo(leo(
				methodName(
					java.lang.String::class.java.getMethod("length").javaScriptLine)))
	}

	@Test
	fun nativeClassMethodNameParameterList() {
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(
				nameName("substring"),
				parameterName(
					listName(ofName(className(nativeName()))),
					plusName(nativeName(className("int"()))),
					plusName(nativeName(className("int"()))))))
			.assertEvalsTo(
				leo(
					methodName(
						java.lang.String::class
							.java
							.getMethod("substring", Integer.TYPE, Integer.TYPE)
							.javaScriptLine)))
	}

	@Test
	fun nativeFieldGetStatic() {
		leo(
			nativeName(className(nameName("java.lang.Integer"))),
			fieldName(nameName("MAX_VALUE")),
			getName(nativeName(nullName())))
			.assertEvalsTo(leo(
				java.lang.Integer::class
					.java
					.getField("MAX_VALUE")
					.get(null)
					.javaScriptLine))
	}

	@Test
	fun nativeConstructorInvoke() {
		leo(
			nativeName(className(nameName("java.lang.StringBuilder"))),
			constructorName(),
			invokeName())
			.assertEvalsTo(leo(StringBuilder().javaScriptLine))
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
			.assertEvalsTo(leo(URL("http", "www.google.com", "/search").javaScriptLine))
	}

	@Test
	fun nativeMethodInvoke() {
		leo(
			nativeName(className(nameName("java.lang.String"))),
			methodName(nameName("length")),
			invokeName(nativeName("Hello, world!")))
			.assertEvalsTo(leo(13.javaScriptLine))
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