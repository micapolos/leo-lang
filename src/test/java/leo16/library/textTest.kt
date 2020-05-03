package leo16.library

import leo15.dsl.*
import leo16.assertGives
import leo16.evaluate_
import org.junit.Test

class TextTest {
	@Test
	fun text() {
		evaluate_ {
			text.dictionary.import
			"Hello".text
		}.assertGives { "Hello".text }

		evaluate_ {
			text.dictionary.import
			"Hello".text.native
		}.assertGives { "Hello".native_ }

		evaluate_ {
			text.dictionary.import
			"Hello, ".text.plus { "world!".text }
		}.assertGives { "Hello, world!".text }

		evaluate_ {
			text.dictionary.import
			"Hello, world!".text.length
		}.assertGives { 13.number }

		evaluate_ {
			text.dictionary.import
			"Hello, world!".text
			replace {
				all { "Hello".text }
				with { "Goodbye".text }
			}
		}.assertGives { "Goodbye, world!".text }
	}
}