package leo32

import leo.base.assertEqualTo
import kotlin.test.Test

class IdTest {
	@Test
	fun codeString() {
		"".id.codeString.assertEqualTo("empty\u0000")
		"jajko".id.codeString.assertEqualTo("jajko\u0000")
		"ja\u0000ko".id.codeString.assertEqualTo("ja\\0ko\u0000")
		"jajko\u0000".id.codeString.assertEqualTo("jajko\\0\u0000")
		"jajko0".id.codeString.assertEqualTo("jajko0\u0000")
		"ja\\ko".id.codeString.assertEqualTo("ja\\\\ko\u0000")
		"ja\\\\ko".id.codeString.assertEqualTo("ja\\\\\\\\ko\u0000")
	}
}