package leo16.library.javascript

import leo15.dsl.*
import leo16.compile_

fun main() {
	dsl
}

val dsl = compile_ {
	use { text }
	use { number }
	use { javascript }

	animation.frame.number.is_ { "animationFrame".text.expression.javascript }
	animation.second.number.is_ { "animationSecond".text.expression.javascript }
	canvas.width.number.is_ { "width".text.expression.javascript }
	canvas.height.number.is_ { "height".text.expression.javascript }
	mouse.x.number.is_ { "mouseX".text.expression.javascript }
	mouse.y.number.is_ { "mouseY".text.expression.javascript }

	text.any
	does { text.expression.javascript.string }

	test {
		"hello".text
		equals_ { quote { javascript { expression { "'hello'".text } } } }
	}

	number.any
	does { number.as_ { text }.expression.javascript }

	test {
		123.number
		equals_ { quote { javascript { expression { "123".text } } } }
	}
}