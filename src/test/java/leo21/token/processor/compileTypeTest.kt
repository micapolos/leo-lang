package leo21.token.processor

import leo.base.assertEqualTo
import leo14.lineTo
import leo14.script
import leo21.type.choice
import leo21.type.doubleLine
import leo21.type.doubleType
import leo21.type.stringLine
import leo21.type.stringType
import leo21.type.type
import kotlin.test.Test

class CompileTypeTest {
	@Test
	fun text() {
		script("text" lineTo script())
			.compileType
			.assertEqualTo(stringType)
	}

	@Test
	fun number() {
		script("number" lineTo script())
			.compileType
			.assertEqualTo(doubleType)
	}

//	@Test
//	fun choices() {
//		script(
//			"choice" lineTo script(
//				"text" lineTo script(),
//				"number" lineTo  script()))
//			.compileType
//			.assertEqualTo(type(choice(stringLine, doubleLine)))
//	}
}