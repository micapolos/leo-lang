package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	stack
}

val stack = dictionary_ {
	any.stack
	push { any }
	gives {
		stack {
			link {
				previous { stack }
				last { push.thing }
			}
		}
	}

	test {
		empty.stack
		push { 1.number }
		gives { stack { pushed { 1.number } } }
	}

	test {
		stack { pushed { 1.number } }
		push { 2.number }
		gives { stack { pushed { 1.number }; pushed { 2.number } } }
	}

	any.stack.reverse
	gives {
		reverse.stack
		fold {
			to { empty.stack }
			step {
				to { any }
				pushed { any }
				giving { to.stack.push { pushed.thing } }
			}
		}
	}

	test {
		stack {
			pushed { 1.number }
			pushed { 2.number }
			pushed { 3.number }
		}.reverse
		gives {
			stack {
				pushed { 3.number }
				pushed { 2.number }
				pushed { 1.number }
			}
		}
	}

	any.stack
	map { taking { any } }
	gives {
		stack.reverse
		fold {
			to { empty.stack }
			step {
				to { any }
				pushed { any }
				giving {
					to.stack
					push { map.take { pushed.thing } }
				}
			}
		}
	}

	test {
		stack {
			pushed { 1.number }
			pushed { 2.number }
			pushed { 3.number }
		}
		map { any.giving { number.ok } }
		gives {
			stack {
				pushed { 1.number.ok }
				pushed { 2.number.ok }
				pushed { 3.number.ok }
			}
		}
	}

	any.stack.length
	gives {
		import { number }
		length.stack
		fold {
			to { 0.number }
			step {
				to { any }
				pushed { any }
				giving { to.number.plus { 1.number } }
			}
		}.length
	}

	test {
		empty.stack.length
		gives { 0.number.length }
	}

	test {
		stack {
			pushed { 0.number }
			pushed { 1.number }
			pushed { 2.number }
		}.length
		gives { 3.number.length }
	}
}
