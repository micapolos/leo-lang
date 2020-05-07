package leo16.sandbox

import leo15.dsl.*
import leo16.leo_

fun main() = leo_ {
	"http://mwiacek.com".text
	url.read.text
	split { by { " się ".text.regular.expression } }
	map {
		item { any }
		giving { zero { item.text.split { " i ".text.regex } } }
	}
}