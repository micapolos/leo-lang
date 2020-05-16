package leo16.library

import leo15.dsl.*
import leo16.compile_

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
}