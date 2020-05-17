package leo16.library

import leo15.dsl.*
import leo16.compile_

fun main() {
	javascript
}

val javascript = compile_ {
	use { base.library }

	empty.javascript
	is_ { "".text.javascript }

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

	any.text.expression.javascript.show
	does {
		"document.body.textContent=".text
		plus { show.javascript.expression.text }
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

	any.text.expression.javascript.string
	does {
		"'".text
		plus { string.javascript.expression.text.comment { escape } }
		plus { "'".text }
		expression.javascript
	}

	test {
		"hello".text.expression.javascript.string
		equals_ { "'hello'".text.expression.javascript }
	}

	any.text.expression.javascript
	in_ { parentheses }
	does {
		"(".text
		plus { javascript.expression.text }
		plus { ")".text }
		expression.javascript
	}

	test {
		"a + b".text.expression.javascript.in_ { parentheses }
		equals_ { "(a + b)".text.expression.javascript }
	}

	any.text.expression.javascript
	plus { any.text.expression.javascript }
	does {
		javascript.in_ { parentheses }.expression.text
		plus { " + ".text }
		plus { plus.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript
		plus { "b".text.expression.javascript }
		equals_ { "(a) + (b)".text.expression.javascript }
	}

	any.text.expression.javascript
	times { any.text.expression.javascript }
	does {
		javascript.in_ { parentheses }.expression.text
		plus { " * ".text }
		plus { times.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript
		times { "b".text.expression.javascript }
		equals_ { "(a) * (b)".text.expression.javascript }
	}

	any.text.expression.javascript.sinus
	does {
		"Math.sin".text
		plus { sinus.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript.sinus
		equals_ { "Math.sin(a)".text.expression.javascript }
	}

	any.text.expression.javascript.cosinus
	does {
		"Math.cos".text
		plus { cosinus.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript.cosinus
		equals_ { "Math.cos(a)".text.expression.javascript }
	}

	any.text.expression.javascript.absolute
	does {
		"Math.abs".text
		plus { absolute.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	any.text.javascript
	fill {
		text { any.text.expression.javascript }
		x { any.text.expression.javascript }
		y { any.text.expression.javascript }
	}
	does {
		javascript.text
		plus {
			"context.fillText(".text
			plus { fill.text.javascript.expression.text }
			plus { ", ".text }
			plus { fill.x.javascript.expression.text }
			plus { ", ".text }
			plus { fill.y.javascript.expression.text }
			plus { ")\n".text }
		}
		javascript
	}

	test {
		empty.javascript
		fill {
			text { "'Hello'".text.expression.javascript }
			x { "10".text.expression.javascript }
			y { "20".text.expression.javascript }
		}
		equals_ { "context.fillText('Hello', 10, 20)\n".text.javascript }
	}

	any.text.javascript
	set { font { any.text.expression.javascript } }
	does {
		javascript.text
		plus { "context.font = ".text }
		plus { set.font.javascript.expression.text }
		plus { "\n".text }
		javascript
	}

	any.text.javascript
	fill {
		circle {
			radius { any.text.expression.javascript }
			x { any.text.expression.javascript }
			y { any.text.expression.javascript }
		}
	}
	does {
		javascript.text
		plus { "context.beginPath()\n".text }
		plus { "context.arc(".text }
		plus { fill.circle.x.javascript.expression.text }
		plus { ", ".text }
		plus { fill.circle.y.javascript.expression.text }
		plus { ", ".text }
		plus { fill.circle.radius.javascript.expression.text }
		plus { ", 0, 2 * Math.PI, false)\n".text }
		plus { "context.fill()\n".text }
		javascript
	}
}