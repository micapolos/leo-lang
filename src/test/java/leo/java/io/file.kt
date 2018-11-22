package leo.java.io

import leo.*
import leo.base.assertEqualTo
import leo.base.theParsed
import leo.java.nio.file.leoPath
import kotlin.test.Test

class FileTest {
	@Test
	fun useBitStream() {
		selector(srcWord, testWord, javaWord, leoWord, javaWord, ioWord, fileWord)
			.leoPath
			.file
			.useBitStream { bitParseWord?.theParsed?.value }
			.assertEqualTo(stringWord)
	}
}