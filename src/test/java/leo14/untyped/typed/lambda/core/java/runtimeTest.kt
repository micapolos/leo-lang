package leo14.untyped.typed.lambda.core.java

import leo.base.assertEqualTo
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.nameName
import leo14.untyped.nativeName
import leo14.untyped.typed.lambda.eval
import leo14.untyped.typed.lambda.lineTo
import leo14.untyped.typed.lambda.nativeTyped
import leo14.untyped.typed.lambda.typed
import java.lang.String
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun className() {
		runtimeJavaCore
			.apply(typed(nativeName lineTo typed(className lineTo typed("int"))))!!
			.eval
			.assertEqualTo(typed(className lineTo Integer.TYPE.nativeTyped))
	}

	@Test
	fun textClass() {
		runtimeJavaCore
			.apply(
				typed(
					nativeName lineTo typed(
						className lineTo typed(
							nameName lineTo "java.lang.String".typed))))!!
			.eval
			.assertEqualTo(typed(className lineTo String::class.java.nativeTyped))
	}

	@Test
	fun classField() {
		runtimeJavaCore
			.apply(
				typed(
					className lineTo Integer::class.java.nativeTyped,
					fieldName lineTo typed(
						nameName lineTo "MAX_VALUE".typed)))!!
			.eval
			.assertEqualTo(
				typed(fieldName lineTo Integer::class.java.getField("MAX_VALUE").nativeTyped))
	}
}