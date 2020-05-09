package leo16

import leo15.dsl.*
import kotlin.test.Test

class LibraryTest {
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
			import { url }
		}
	}
}