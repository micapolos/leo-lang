package leo13.js.compiler

val String.jsInHtml
	get() = "<script>window.onload=function(){$this}</script>"
