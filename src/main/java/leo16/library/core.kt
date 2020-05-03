package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val core = dictionary_ {
	test { zero.negate.gives { negate { zero } } }
}