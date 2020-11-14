package leo21.token.body

import leo14.Fragment
import leo14.FragmentParent
import leo14.Script
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.compiled.script

val Body.printScript: Script
	get() =
		compiled.script

val BodyCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(body.printScript)

val BodyCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is BodyCompiler.Parent.BodyCompilerName ->
				bodyCompiler.printFragment.parent(begin(name))
			is BodyCompiler.Parent.BodyCompilerDo ->
				bodyCompiler.printFragment.parent(begin("do"))
		}
