package leo16

import leo.base.assertEqualTo
import leo15.dsl.*
import leo16.names.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.Test

class PrintingTest {
	@Test
	fun printing() {
		val bos = ByteArrayOutputStream()
		System.setOut(PrintStream(bos))
		evaluate_ {
			printing.import
			zero.printing
		}.assertGives { zero }
		System.setOut(null)
		bos.toString().assertEqualTo(_zero().toString() + "\n")
	}
}