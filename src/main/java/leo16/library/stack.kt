package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	stack
}

val stack = dictionary_ {
	any.stack
	plus { any }
	gives {
		stack {
			link {
				previous { stack }
				last { plus.thing }
			}
		}
	}

	test {
		empty.stack
		plus { 1.number }
		gives { stack { item { 1.number } } }
	}

	test {
		stack { item { 1.number } }
		plus { 2.number }
		gives { stack { item { 1.number }; item { 2.number } } }
	}

	any.stack.reverse
	gives {
		reverse.stack
		fold {
			to { empty.stack }
			step {
				to { any }
				item { any }
				giving { to.stack.plus { item.thing } }
			}
		}
	}

	test {
		stack {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}.reverse
		gives {
			stack {
				item { 3.number }
				item { 2.number }
				item { 1.number }
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
				item { any }
				giving {
					to.stack
					plus { map.take { item.thing } }
				}
			}
		}
	}

	test {
		stack {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}
		map { any.giving { number.ok } }
		gives {
			stack {
				item { 1.number.ok }
				item { 2.number.ok }
				item { 3.number.ok }
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
				item { any }
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
			item { 0.number }
			item { 1.number }
			item { 2.number }
		}.length
		gives { 3.number.length }
	}
}
