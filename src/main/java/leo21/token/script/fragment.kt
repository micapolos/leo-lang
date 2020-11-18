package leo21.token.script

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent

val ScriptCompiler.printFragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(script)

val ScriptParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is ScriptCompilerNameScriptParent -> scriptCompiler.printFragment.parent(begin(name))
		}

