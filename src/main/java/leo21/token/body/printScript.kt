package leo21.token.body

import leo14.Fragment
import leo14.FragmentParent
import leo14.Script
import leo14.begin
import leo14.fragment
import leo14.lineTo
import leo14.parent
import leo14.plus
import leo14.script
import leo21.compiled.script
import leo21.type.script

val Body.printScript: Script
	get() =
		compiled.script

val BodyCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(body.printScript)

val BodyCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is BodyCompiler.Parent.BodyName ->
				bodyCompiler.printFragment.parent(begin(name))
			is BodyCompiler.Parent.BodyDo ->
				bodyCompiler.printFragment.parent(begin("do"))
			is BodyCompiler.Parent.FunctionIt ->
				functionItCompiler.printFragment.parent(begin("it"))
		}

val FunctionCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(script())

val FunctionItCompiler.printScript: Script
	get() =
		script("it" lineTo type.script)

val FunctionItCompiler.printFragment: Fragment
	get() =
		parent.printFragment.plus(printScript)

val FunctionItDoesCompiler.printScript: Script
	get() =
		parent.printScript.plus("does" lineTo arrowCompiled.arrow.rhs.script)

val FunctionItDoesCompiler.printFragment: Fragment
	get() =
		parent.printFragment.plus(printScript)

val FunctionCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is FunctionCompiler.Parent.Define -> defineCompiler.printFragment.parent(begin("define"))
			is FunctionCompiler.Parent.Body -> bodyCompiler.printFragment.parent(begin("define"))
		}

val DefineCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(script())

val DefineCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is DefineCompiler.Parent.Body -> bodyCompiler.printFragment.parent(begin("define"))
		}
