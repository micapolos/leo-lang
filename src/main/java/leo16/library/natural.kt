package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(natural)
}

val natural = dsl_ {
	use { leo }

	any { natural }
	is_ {
		do_ {
			natural {
				zero
				or { successor { of { lazy_ { repeat } } } }
			}
		}
	}

	test {
		any { natural }
		matches { natural { zero } }
	}

	test {
		any { natural }
		matches { natural { successor { of { natural { zero } } } } }
	}

	test {
		any { natural }
		matches { natural { successor { of { natural { successor { of { natural { zero } } } } } } } }
	}

	natural.any.next
	does { natural { successor { of { next.natural } } } }

	test {
		zero.natural.next
		equals_ { natural { successor { of { natural { zero } } } } }
	}

	test {
		zero.natural.next.next
		equals_ { natural { successor { of { natural { successor { of { natural { zero } } } } } } } }
	}

	natural { successor { of { natural.any } } }.previous
	does { previous.natural.successor.of.natural }

	test { zero.natural.previous.equals_ { zero.natural.make { previous } } }
	test { zero.natural.next.previous.equals_ { zero.natural } }
	test { zero.natural.next.next.previous.equals_ { zero.natural.next } }

	natural.any
	plus { natural.any }
	does {
		plus.natural.equals_ { zero.natural }
		match {
			true_ { natural }
			false_ {
				natural.next
				plus { plus.natural.previous }
				repeat
			}
		}
	}

	test { zero.natural.plus { zero.natural }.equals_ { zero.natural } }
	test { zero.natural.plus { zero.natural.next }.equals_ { zero.natural.next } }
	test { zero.natural.next.plus { zero.natural }.equals_ { zero.natural.next } }
	test { zero.natural.next.plus { zero.natural.next }.equals_ { zero.natural.next.next } }
}