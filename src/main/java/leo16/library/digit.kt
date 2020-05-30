package leo16.library

import leo.base.print
import leo15.dsl.*
import leo16.compile_

fun main() {
	digit.value.print
}

val digit = compile_ {
	use { character }
	use { text }
	use { list }
	use { number }

	any.digit.check.does { false_.boolean }

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
					word { word { word { check { name.thing.digit } } } }
					word { is_ { true_.boolean } }
				}
				this_ {
					name.thing.digit.number
					word { is_ { number } }
				}
				this_ {
					name.thing.digit.character
					word { is_ { number.text.character } }
				}
				this_ {
					number.digit
					word { is_ { name.thing.digit } }
				}
				this_ {
					number.text.character.digit
					word { is_ { name.thing.digit } }
				}
			}
		}
	}
	flat.thing.compile

	test { ten.digit.check.equals_ { false_.boolean } }
	test { seven.digit.check.equals_ { true_.boolean } }
	test { seven.digit.number.equals_ { 7.number } }
	test { seven.digit.character.equals_ { "7".text.character } }
	test { 7.number.digit.equals_ { seven.digit } }
	test { "7".text.character.digit.equals_ { seven.digit } }
}