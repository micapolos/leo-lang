package leo14.untyped.dsl2.library

import leo14.untyped.dsl2.*

val core = library_ {
	nothing.gives { nothing_ }
	anything.clear.gives { nothing }

	anything
	replace { anything }
	does { given.replace.content }
}