package leo16.library.demo

import leo15.dsl.*
import leo16.compile_

val javascript = compile_ {
	use { javascript.library }

	"context.fillText(mouseX + ':' + mouseY, 10, 10)".text.javascript.animated.run
}