package leo16.sandbox

import leo15.dsl.*
import leo16.leo_

fun main() = leo_ {
	0.number
	fold {
		"http://mwiacek.com".text.url.get.lines
		map { giving { given.text.length } }
		giving {
			given.folded.number
			plus { given.next.number }
		}
	}
}