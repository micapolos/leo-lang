package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val list = dictionary_ {
	any.list.last
	gives {
		given.last.list.match {
			empty.is_ { given.last }
			any.link.gives { given.link.last }
		}
	}

	any.list.previous
	gives {
		given.previous.list.match {
			empty.is_ { given.previous }
			any.link.gives { given.link.previous }
		}
	}

	any.list
	append { any }
	gives {
		given.list.thing
		this_ { given.append.thing }
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
		given.reverse.list
		fold {
			to { list }
			step {
				to { any }
				item { any }
				giving { given.to.list.append { given.item.thing } }
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
		map.is_ { given.map }
		given.list.reverse
		fold {
			to { list }
			step {
				to { any }
				item { any }
				giving {
					given.to.list
					append { map.taking.give { given.item } }
				}
			}
		}
	}

	test {
		list { 1.number; 2.number; 3.number }
		map { any.item.giving { given.item } }
		gives { list { item { 1.number }; item { 2.number }; item { 3.number } } }
	}

	any.list.length
	gives {
		number.import
		given.length.list
		fold {
			to { 0.number }
			step {
				to { any }
				item { any }
				giving {
					given.to.number.plus { 1.number }
				}
			}
		}.length
	}

	test { list.length.gives { 0.number.length } }
	test { list { 0.number; 1.number; 2.number }.length.gives { 3.number.length } }
}
