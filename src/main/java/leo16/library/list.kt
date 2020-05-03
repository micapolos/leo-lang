package leo16.library

import leo15.dsl.*
import leo16.dictionary_
import leo16.run_

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
}

fun main() = run_ { list }
