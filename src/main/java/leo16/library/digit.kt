package leo16.library

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	digit.value.print
}

val digit = compile_ {
	use { character.library }
	use { text.library }
	use { list.library }
	use { number.library }

	list {
		item { 0.number.name { zero } }
		item { 1.number.name { one } }
		item { 2.number.name { two } }
		item { 3.number.name { three } }
		item { 4.number.name { four } }
		item { 5.number.name { five } }
		item { 6.number.name { six } }
		item { 7.number.name { seven } }
		item { 8.number.name { eight } }
		item { 9.number.name { nine } }
	}
	map {
		function {
			any
			does {
				this_ {
					name.content.digit.number
					word { is_ { number } }
				}
				this_ {
					name.content.digit.character
					word { is_ { number.text.character } }
				}
				this_ {
					number.digit
					word { is_ { name.content.digit } }
				}
				this_ {
					number.text.character.digit
					word { is_ { name.content.digit } }
				}
			}
		}
	}
	flat.content.compile

	test { seven.digit.number.equals_ { 7.number } }
	test { seven.digit.character.equals_ { "7".text.character } }
	test { 7.number.digit.equals_ { seven.digit } }
	test { "7".text.character.digit.equals_ { seven.digit } }
}