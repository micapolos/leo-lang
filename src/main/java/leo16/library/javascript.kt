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
}