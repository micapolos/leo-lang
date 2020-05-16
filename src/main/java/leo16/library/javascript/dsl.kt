package leo16.library.javascript

import leo15.dsl.*
import leo16.compile_

fun main() {
	dsl
}

val dsl = compile_ {
	use { javascript.library }

	time.is_ { "time".text.expression.javascript }
	mouse.x.is_ { "mouseX".text.expression.javascript }
	mouse.y.is_ { "mouseY".text.expression.javascript }

	any.native.text
	does { text.expression.javascript.string }

	test {
		"hello".text
		equals_ { quote { javascript { expression { "'hello'".text } } } }
	}

	any.native.number
	does { number.as_ { text }.expression.javascript }

	test {
		123.number
		equals_ { quote { javascript { expression { "123".text } } } }
	}
}