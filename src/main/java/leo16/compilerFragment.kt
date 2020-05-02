package leo16

import leo14.*

val Compiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(compiled.value.printScript.leo14Script)

val CompilerParent.fragmentParent: FragmentParent
	get() =
		compiler.fragment.parent(begin(word))
