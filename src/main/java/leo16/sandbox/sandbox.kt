package leo16.sandbox

import leo15.dsl.*
import leo16.print_

fun main() = print_ {
	text.import
	reflection.import

	"Hello, world!".text
	split { regex { " ".text } }
}