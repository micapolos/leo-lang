package leo16.library

import leo15.dsl.*
import leo16.library_

fun main() {
	library_(javascript)
}

val javascript = dsl_ {
	use { base }

	javascript.any.is_ { text.any.javascript }
	expression.javascript.any.is_ { text.any.expression.javascript }

	empty.javascript
	is_ { "".text.javascript }

	javascript.any.html
	does {
		"<script>window.onload=function(){".text
		plus { html.javascript.text }
		plus { "}</script>".text }
		html
	}

	javascript.any.run
	does {
		use { html }
		run.javascript.html.open
	}

	expression.javascript.any.show
	does {
		"document.body.textContent=".text
		plus { show.javascript.expression.text }
		javascript.run
	}

	javascript.any.animated
	does {
		"".text
		plus {
			"""
      document.body.style = "position:absolute; width:100%; height:100%; margin:0"
			const canvas = document.createElement('canvas')
			const scale = window.devicePixelRatio
			canvas.style = "position:absolute; width:100%; height: 100%"
			const context = canvas.getContext('2d')
      document.body.appendChild(canvas)
			var animationFrame = 0
			var width = 0
			var height = 0
			function resize() {
			  width = window.innerWidth
				height = window.innerHeight
			  canvas.width = width * scale
        canvas.height = height * scale
        context.scale(scale, scale)
  			context.font = "20px Menlo"
			}
			resize()
			var mouseX = width / 2
			var mouseY = height / 2
			window.onresize = resize;
			function handleMouseEvent(event) {
			  mouseX = event.offsetX
			  mouseY = event.offsetY
			}
			canvas.onmousemove = handleMouseEvent
			canvas.ommouseenter = handleMouseEvent
			const animate = function(time) {
			  const animationSecond = time / 1000
			  animationFrame++;
			  context.clearRect(0, 0, width, height);
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

	expression.javascript.any.string
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

	expression.javascript.any
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

	expression.javascript.any
	plus { expression.javascript.any }
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

	expression.javascript.any
	minus { expression.javascript.any }
	does {
		javascript.in_ { parentheses }.expression.text
		plus { " - ".text }
		plus { minus.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript
		minus { "b".text.expression.javascript }
		equals_ { "(a) - (b)".text.expression.javascript }
	}

	expression.javascript.any
	times { expression.javascript.any }
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

	expression.javascript.any
	modulo { expression.javascript.any }
	does {
		javascript.in_ { parentheses }.expression.text
		plus { " % ".text }
		plus { modulo.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript
		modulo { "b".text.expression.javascript }
		equals_ { "(a) % (b)".text.expression.javascript }
	}

	expression.javascript.any.negate
	does {
		"-".text
		plus { negate.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript.negate
		equals_ { "-(a)".text.expression.javascript }
	}

	expression.javascript.any.sinus
	does {
		"Math.sin".text
		plus { sinus.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript.sinus
		equals_ { "Math.sin(a)".text.expression.javascript }
	}

	expression.javascript.any.cosinus
	does {
		"Math.cos".text
		plus { cosinus.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	test {
		"a".text.expression.javascript.cosinus
		equals_ { "Math.cos(a)".text.expression.javascript }
	}

	expression.javascript.any.absolute
	does {
		"Math.abs".text
		plus { absolute.javascript.in_ { parentheses }.expression.text }
		expression.javascript
	}

	javascript.any
	fill {
		text { expression.javascript.any }
		x { expression.javascript.any }
		y { expression.javascript.any }
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

	javascript.any
	set { font { expression.javascript.any } }
	does {
		javascript.text
		plus { "context.font = ".text }
		plus { set.font.javascript.expression.text }
		plus { "\n".text }
		javascript
	}

	javascript.any
	fill {
		circle {
			radius { expression.javascript.any }
			x { expression.javascript.any }
			y { expression.javascript.any }
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