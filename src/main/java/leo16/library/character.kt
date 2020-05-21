package leo16.library

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	character.value.print
}

val character = compile_ {
	use { reflection.library }
	use { string.native.library }
	use { character.native.library }

}