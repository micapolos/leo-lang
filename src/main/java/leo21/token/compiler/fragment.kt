package leo21.token.compiler

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.compiled.script

val TokenCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(lineCompiler.compiled.script)

val CompiledParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is CompilerNameCompiledParent -> compiler.fragment.parent(begin(name))
			is CompilerDoCompiledParent -> compiler.fragment.parent(begin("do"))
		}

