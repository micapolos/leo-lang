package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	text.load.import
	reflection.load.import

	"Hello, world!".text
	split { regex { " ".text } }
	array.list
}