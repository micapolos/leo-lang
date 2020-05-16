package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	javascript
}

val javascript = compile_ {
	use { base.library }

	any.text.javascript.html
	does {
		"<script>window.onload=function(){".text
		plus { html.javascript.text }
		plus { "}</script>".text }
		html
	}

	any.text.javascript.run
	does {
		use { html.library }
		run.javascript.html.open
	}

	any.text.javascript.show
	does {
		"document.body.textContent=".text
		plus { show.javascript.text }
		javascript.run
	}

	any.text.javascript.animated
	does {
		"".text
		plus {
			"""
			const canvas = document.createElement('canvas')
		  canvas.width = 640
      canvas.height = 480
			const context = canvas.getContext('2d')
      document.body.appendChild(canvas)
			var mouseX = 0
			var mouseY = 0
			function handleMouseEvent(event) {
			  mouseX = event.offsetX
			  mouseY = event.offsetY
			}
			canvas.addEventListener('mousemove', handleMouseEvent, false)
			canvas.addEventListener('mouseenter', handleMouseEvent, false)
			const animate = function(time) {
			  context.clearRect(0, 0, 640, 480);
			""".text
		}
		plus { animated.javascript.text }
		plus {
			"""
        window.requestAnimationFrame(animate)
			}
			animate()
			""".text
		}
		javascript
	}

	any.text.javascript.string
	does {
		"'".text
		plus { string.javascript.text.comment { escape } }
		plus { "'".text }
		javascript
	}

	test {
		"hello".text.javascript.string
		equals_ { "'hello'".text.javascript }
	}

	any.text.javascript
	in_ { parentheses }
	does {
		"(".text
		plus { javascript.text }
		plus { ")".text }
		javascript
	}

	test {
		"a + b".text.javascript.in_ { parentheses }
		equals_ { "(a + b)".text.javascript }
	}

	any.text.javascript
	plus { any.text.javascript }
	does {
		javascript.in_ { parentheses }.text
		plus { " + ".text }
		plus { plus.javascript.in_ { parentheses }.text }
		javascript
	}

	test {
		"a".text.javascript
		plus { "b".text.javascript }
		equals_ { "(a) + (b)".text.javascript }
	}

	comment { put { last } }

	any.text
	does { text.javascript.string }
}