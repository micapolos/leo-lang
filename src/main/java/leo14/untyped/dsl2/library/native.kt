package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.library_
import leo14.untyped.dsl2.nothing_
import leo14.untyped.dsl2.run_

val native = library_ {
	nothing_
}

fun main() = run_(native)