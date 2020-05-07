package leo16.sandbox

import leo15.dsl.*
import leo16.leo_

fun main() = leo_ {
	"http://mwiacek.com".text
	url.read.text
	line.list
	map {
		item { any }
		giving { item.text.length.number }
	}
	fold {
		to { 0.number }
		step {
			to { any }
			item { any }
			giving { to.number.plus { item.number } }
		}
	}
}