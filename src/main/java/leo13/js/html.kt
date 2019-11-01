package leo13.js

import leo13.base.linesString

val String.jsInHtml
	get() =
		linesString(
			"<script>",
			"window.onload = function() {",
			"",
			"var __stack = []",
			"",
			"function __bind(arg, fn) {",
			"  __stack.unshift(arg)",
			"  try {",
			"    return fn()",
			"  } finally {",
			"    __stack.shift()",
			"  }",
			"}",
			"",
			"function __bound(index) {",
			"  return __stack[index]",
			"}",
			"",
			"function __lambda(fn) {",
			"  return function(arg) {",
			"    return __bind(arg, fn)",
			"  }",
			"}",
			"",
			this,
			"}",
			"</script>")
