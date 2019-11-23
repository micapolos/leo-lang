package leo14.typed.compiler.js

import leo14.lineTo
import leo14.literal
import leo14.script

val helloWorld = script(
	literal("Hello, world!"))

val expression = script(
	"javascript" lineTo script(
		"expression" lineTo script(
			literal("2 + 2"))))

val invokeAlert = script(
	"javascript" lineTo script(
		"expression" lineTo script(
			literal("window.alert"))),
	"invoke" lineTo script(
		"javascript" lineTo script(
			literal("Hello, world!"))))

val redBackgroundColor = script(
	"javascript" lineTo script(
		"expression" lineTo script(
			literal("document.body.style"))),
	"set" lineTo script(
		literal("backgroundColor")),
	"to" lineTo script(
		"javascript" lineTo script(
			"expression" lineTo script(
				literal("\"red\"")))))

val div = script(
	"javascript" lineTo script(
		"expression" lineTo script(
			literal("document.body.appendChild"))),
	"invoke" lineTo script(
		"javascript" lineTo script(
			"expression" lineTo script(
				literal("document.createElement('div')"))),
		"set" lineTo script(literal("textContent")),
		"to" lineTo script(
			"javascript" lineTo script(literal("jajeczko")))))

val search = script(
	"javascript" lineTo script(
		"expression" lineTo script(
			literal("window.location"))),
	"set" lineTo script(literal("href")),
	"to" lineTo script(
		"javascript" lineTo script(
			literal("http://www.google.com/search?q=jajeczko"))))

fun main() = search.open