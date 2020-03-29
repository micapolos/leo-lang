package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.library_
import leo14.untyped.dsl2.run_

val prelude = library_ {
	core()
	native()
	text()
	system()
	draw()
}

fun main() = run_(prelude)