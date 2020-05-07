package leo16.library

import leo15.dsl.*
import leo16.dictionary_

val list = dictionary_ {
	empty.list
	is_ { quote { list } }

	any.list
	append { item { any } }
	gives {
		list.thing
		this_ { append.item }
		list
	}

	test {
		list
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
		reverse.list
		fold {
			to { list }
			step {
				to { any }
				item { any }
				giving { to.list.append { item } }
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
		list.reverse
		fold {
			to { empty.list }
			step {
				to { any }
				item { any }
				giving {
					to.list
					append { item { map.take { item } } }
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
		map { any.item.giving { item.number.ok } }
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
		number.import
		length.list
		fold {
			to { 0.number }
			step {
				to { any }
				item { any }
				giving { to.number.plus { 1.number } }
			}
		}.length
	}

	test { list.length.gives { 0.number.length } }
	test {
		list {
			item { 0.number }
			item { 1.number }
			item { 2.number }
		}.length.gives { 3.number.length }
	}
}
