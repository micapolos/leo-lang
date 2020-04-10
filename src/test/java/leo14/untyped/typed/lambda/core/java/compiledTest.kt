package leo14.untyped.typed.lambda.core.java

import leo.base.assertEqualTo
import leo14.lambda2.*
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lambda.compiled
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.nativeType
import leo14.untyped.typed.textType
import leo14.untyped.typed.type
import kotlin.test.Test

class CompiledTest {
	@Test
	fun className() {
		compiledJavaCore
			.apply(
				type(nativeName lineTo type(className lineTo type("int")))
					.compiled(nil))!!
			.term.eval.value
			.assertEqualTo("int.class")
	}

	@Test
	fun textClass() {
		compiledJavaCore
			.apply(
				type(nativeName lineTo type(className lineTo type(nameName lineTo textType)))
					.compiled("\"java.lang.String\"".valueTerm))!!
			.term.eval.value
			.assertEqualTo("leo14.MainKt.class.getClassLoader().loadClass(\"java.lang.String\")")
	}

	@Test
	fun classField() {
		compiledJavaCore
			.apply(
				type(
					className lineTo nativeType,
					fieldName lineTo type(nameName lineTo textType))
					.compiled(pair("int.class".valueTerm)("\"MAX_VALUE\"".valueTerm)))!!
			.term.eval.value
			.assertEqualTo("int.class.getField(\"MAX_VALUE\")")
	}
}