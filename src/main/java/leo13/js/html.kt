package leo13.js

import leo13.base.linesString

val String.jsInHtml
	get() =
		linesString(
			"<script>",
			"window.onload = function() {",
			this,
			"}",
			"</script>")
