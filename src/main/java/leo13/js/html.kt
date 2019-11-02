package leo13.js

val String.jsInHtml
	get() = "<script>window.onload=function(){$this}</script>"
