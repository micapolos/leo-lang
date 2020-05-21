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
		item { 0.number.named { zero } }
		item { 1.number.named { one } }
		item { 2.number.named { two } }
		item { 3.number.named { three } }
		item { 4.number.named { four } }
		item { 5.number.named { five } }
		item { 6.number.named { six } }
		item { 7.number.named { seven } }
		item { 8.number.named { eight } }
		item { 9.number.named { nine } }
	}
	map {
		function {
			any
			does {
				this_ {
					named.content.digit.number
					word { is_ { number } }
				}
				this_ {
					named.content.digit.character
					word { is_ { number.text.character } }
				}
				this_ {
					number.digit
					word { is_ { named.content.digit } }
				}
				this_ {
					number.text.character.digit
					word { is_ { named.content.digit } }
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