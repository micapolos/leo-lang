package leo16.sandbox

import leo15.dsl.*
import leo16.leo_

fun main() = leo_ {
	"http://mwiacek.com".text.url.get.text.line.list
	map { giving { given.text.length.number } }
	fold {
		from { 0.number }
		step {
			giving {
				given.folded.number
				plus { given.next.number }
			}
		}
	}
}