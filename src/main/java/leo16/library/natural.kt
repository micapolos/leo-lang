package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(natural)
}

val natural = dsl_ {
	use { leo }

	natural.any
	is_ {
		do_ {
			zero
			or { lazy_ { repeat }.next }
			natural
		}
	}

	test { natural.any.matches { zero.natural } }
	test { natural.any.matches { zero.natural.next.natural } }
	test { natural.any.matches { zero.natural.next.natural.next.natural } }

	natural.any
	next.natural
	previous.natural
	does { natural.previous.natural.next.natural }

	test { zero.natural.previous.natural.equals_ { zero.natural.previous.natural } }
	test { zero.natural.next.natural.previous.natural.equals_ { zero.natural } }
	test { zero.natural.next.natural.next.natural.previous.natural.equals_ { zero.natural.next.natural } }

	natural.any
	plus { natural.any }
	does {
		plus.natural.equals_ { zero.natural }
		match {
			true_ { natural }
			false_ {
				natural.next.natural
				plus { plus.natural.previous.natural }
				repeat
			}
		}
	}

	test { zero.natural.plus { zero.natural }.equals_ { zero.natural } }
	test { zero.natural.next.natural.plus { zero.natural }.equals_ { zero.natural.next.natural } }
	test { zero.natural.plus { zero.natural.next.natural }.equals_ { zero.natural.next.natural } }
	test { zero.natural.next.natural.plus { zero.natural.next.natural }.equals_ { zero.natural.next.natural.next.natural } }
}