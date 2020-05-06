package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val list = dictionary_ {
	any.list.last
	gives {
		last.list.match {
			empty.is_ { last }
			any.link.gives { link.last }
		}
	}

	any.list.previous
	gives {
		previous.list.match {
			empty.is_ { previous }
			any.link.gives { link.previous }
		}
	}

	any.list
	append { any }
	gives {
		list.thing
		this_ { append.thing }
		list
	}

	test {
		list
		append { 1.number }
		gives { list { 1.number } }
	}

	test {
		list { 1.number; }
		append { 2.number }
		gives { list { 1.number; 2.number } }
	}

	any.list.reverse
	gives {
		reverse.list
		fold {
			to { list }
			step {
				to { any }
				item { any }
				giving { to.list.append { item.thing } }
			}
		}
	}

	test {
		list { 1.number; 2.number; 3.number }.reverse
		gives { list { 3.number; 2.number; 1.number } }
	}

	any.list
	map { taking { any.item } }
	gives {
		list.reverse
		fold {
			to { quote { list } }
			step {
				to { any }
				item { any }
				giving {
					to.list
					append { map.taking.give { item } }
				}
			}
		}
	}

	test {
		list { 1.number; 2.number; 3.number }
		map { any.item.giving { item } }
		gives { list { item { 1.number }; item { 2.number }; item { 3.number } } }
	}

	any.list.length
	gives {
		number.import
		length.list
		fold {
			to { 0.number }
			step {
				to { any }
				item { any }
				giving {
					to.number.plus { 1.number }
				}
			}
		}.length
	}

	test { list.length.gives { 0.number.length } }
	test { list { 0.number; 1.number; 2.number }.length.gives { 3.number.length } }
}
