package leo16.library

import leo15.dsl.*
import leo16.library_

val listLibrary = library_ {
	any.list.last.gives {
		given.last.list.match {
			empty.is_ { given.last }
			any.link.gives { given.link.last }
		}
	}

	any.list.previous.gives {
		given.previous.list.match {
			empty.is_ { given.previous }
			any.link.gives { given.link.previous }
		}
	}
}