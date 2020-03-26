package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val core = library_ {
	nothing.gives
	anything.clear.gives

	anything
	replace { anything }
	does { given.replace.content }
}