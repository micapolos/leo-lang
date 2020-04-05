package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.library_
import leo14.untyped.dsl2.run_

val prelude = library_ {
	core()
	native()
	list()
	text()
	system()
	url()
}

fun main() = run_(prelude)