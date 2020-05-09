package leo16.library

import leo15.dsl.*
import leo16.dictionary_

fun main() {
	list
}

val list = dictionary_ {
	any.list
	append { item { any } }
	gives {
		list {
			link {
				previous { list }
				last { append.item }
			}
		}
	}

	test {
		empty.list
		append { item { 1.number } }
		gives { list { item { 1.number } } }
	}

	test {
		list { item { 1.number } }
		append { item { 2.number } }
		gives { list { item { 1.number }; item { 2.number } } }
	}

	any.list.reverse
	gives {
		empty.list
		fold {
			reverse.list
			step {
				folded { any }
				item { any }
				giving { folded.list.append { item } }
			}
		}
	}

	test {
		list {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}.reverse
		gives {
			list {
				item { 3.number }
				item { 2.number }
				item { 1.number }
			}
		}
	}

	any.list
	map { taking { any.item } }
	gives {
		empty.list
		fold {
			list.reverse
			step {
				folded { any }
				item { any }
				giving {
					folded.list
					append { map.take { item } }
				}
			}
		}
	}

	test {
		list {
			item { 1.number }
			item { 2.number }
			item { 3.number }
		}
		map { any.item.giving { item.number.ok.item } }
		gives {
			list {
				item { 1.number.ok }
				item { 2.number.ok }
				item { 3.number.ok }
			}
		}
	}

	any.list.length
	gives {
		import { number }
		0.number
		fold {
			length.list
			step {
				folded { any }
				item { any }
				giving { folded.number.plus { 1.number } }
			}
		}.length
	}

	test {
		empty.list.length
		gives { 0.number.length }
	}

	test {
		list {
			item { 0.number }
			item { 1.number }
			item { 2.number }
		}.length
		gives { 3.number.length }
	}
}
