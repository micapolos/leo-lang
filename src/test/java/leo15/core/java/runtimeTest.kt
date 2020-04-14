package leo15.core.java

import leo.base.assertEqualTo
import leo14.untyped.className
import leo14.untyped.fieldName
import leo14.untyped.javaName
import leo14.untyped.nameName
import leo15.eval
import leo15.lineTo
import leo15.typed
import leo15.valueJavaTyped
import java.lang.String
import kotlin.test.Test

class RuntimeTest {
	@Test
	fun className() {
		runtimeJavaCore
			.apply(typed(javaName lineTo typed(className lineTo typed("int"))))!!
			.eval
			.assertEqualTo(typed(className lineTo Integer.TYPE.valueJavaTyped))
	}

	@Test
	fun textClass() {
		runtimeJavaCore
			.apply(
				typed(
					javaName lineTo typed(
						className lineTo typed(
							nameName lineTo "java.lang.String".typed))))!!
			.eval
			.assertEqualTo(typed(className lineTo String::class.java.valueJavaTyped))
	}

	@Test
	fun classField() {
		runtimeJavaCore
			.apply(
				typed(
					className lineTo Integer::class.java.valueJavaTyped,
					fieldName lineTo typed(
						nameName lineTo "MAX_VALUE".typed)))!!
			.eval
			.assertEqualTo(
				typed(fieldName lineTo Integer::class.java.getField("MAX_VALUE").valueJavaTyped))
	}
}