package leo16.library

import leo15.dsl.*
import leo16.assertGives
import leo16.evaluate_
import org.junit.Test

class NumberTest {
	@Test
	fun numbers() {
		evaluate_ { number.dictionary.import; 123.number }.assertGives { 123.number }
		evaluate_ { number.dictionary.import; 123.number.native }.assertGives { 123.native_ }
		evaluate_ { number.dictionary.import; 2.number.plus { 3.number } }.assertGives { 5.number }
		evaluate_ { number.dictionary.import; 5.number.minus { 3.number } }.assertGives { 2.number }
		evaluate_ { number.dictionary.import; 2.number.times { 3.number } }.assertGives { 6.number }
	}
}
