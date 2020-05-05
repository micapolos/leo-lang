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
		list
		fold {
			given.reverse.list
			giving { given.folded.list.append { given.next.thing } }
		}
	}

	test {
		list { 1.number; 2.number; 3.number }.reverse
		gives { list { 3.number; 2.number; 1.number } }
	}

	any.list
	map { any.giving }
	gives {
		map.is_ { given.map }
		list
		fold {
			given.list.reverse
			giving { given.folded.list.append { map.giving.give { given.next.thing } } }
		}
	}

	test {
		list { 1.number; 2.number; 3.number }
		map { giving { given } }
		gives { list { given { 1.number }; given { 2.number }; given { 3.number } } }
	}

	any.list.length
	gives {
		number.import
		0.number
		fold {
			given.length.list
			giving { given.folded.number.plus { 1.number } }
		}.length
	}

	test { list.length.gives { 0.number.length } }
	test { list { 0.number; 1.number; 2.number }.length.gives { 3.number.length } }
}
