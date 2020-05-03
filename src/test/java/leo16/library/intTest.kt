package leo16.library

import leo15.dsl.*
import leo16.assertGives
import leo16.evaluate_
import kotlin.test.Test

class IntTest {
	@Test
	fun ints() {
		evaluate_ { int.dictionary.import; 123.int }.assertGives { 123.int }
		evaluate_ { int.dictionary.import; 123.int.native }.assertGives { 123.native_ }
		evaluate_ { int.dictionary.import; 2.int.plus { 3.int } }.assertGives { 5.int }
		evaluate_ { int.dictionary.import; 5.int.minus { 3.int } }.assertGives { 2.int }
		evaluate_ { int.dictionary.import; 2.int.times { 3.int } }.assertGives { 6.int }
	}
}