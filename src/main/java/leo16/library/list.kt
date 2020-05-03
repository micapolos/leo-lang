package leo16.library

import leo15.dsl.*
import leo16.value_

val list = value_ {
	dictionary {
		define {
			any.list.last
			gives {
				given.last.list.match {
					empty.is_ { given.last }
					any.link.gives { given.link.last }
				}
			}
		}

		define {
			any.list.previous
			gives {
				given.previous.list.match {
					empty.is_ { given.previous }
					any.link.gives { given.link.previous }
				}
			}
		}

		define {
			any.list
			append { any }
			gives {
				given.list.thing
				this_ { given.append.thing }
				list
			}
		}
	}
}