package leo16.library

import leo15.dsl.*
import leo16.evaluate_
import kotlin.test.Test

class IndexTest {
	@Test
	fun test() {
		evaluate_ {
			import { base }
			import { bit }
			import { core }
			import { int }
			import { list }
			import { reflection }
			import { number }
			import { text }
		}
	}
}