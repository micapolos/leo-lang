package leo16

import leo15.dsl.*
import kotlin.test.Test

class LibraryTest {
	@Test
	fun test() {
		evaluate_ {
			use { base }
			use { bit }
			use { core }
			use { int }
			use { list }
			use { reflection }
			use { number }
			use { text }
			use { character }
			use { url }
		}
	}
}