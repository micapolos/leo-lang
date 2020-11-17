package leo21.token.body

import leo13.map
import leo14.Fragment
import leo14.FragmentParent
import leo14.Script
import leo14.ScriptLine
import leo14.begin
import leo14.fragment
import leo14.lineTo
import leo14.parent
import leo14.plus
import leo14.script
import leo21.compiled.script
import leo21.token.evaluator.printFragment
import leo21.token.type.compiler.Lines
import leo21.type.Line
import leo21.type.name
import leo21.type.script
import leo21.type.scriptLine

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
			is BodyCompiler.Parent.FunctionItDoes ->
				functionItCompiler.printFragment.parent(begin("does"))
			is BodyCompiler.Parent.SwitchCase ->
				switchCompiler.printFragment.parent(begin(case.name))
		}

val SwitchCompiler.printFragment: Fragment
	get() =
		parentBodyCompiler.printFragment
			.parent(begin("switch"))
			.fragment(caseFieldStack.map { scriptLine }.script)

val FunctionCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(script())

val FunctionItCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(script("it" lineTo type.script))

val FunctionItDoesCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(arrowCompiled.arrow.script)

val FunctionCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is FunctionCompiler.Parent.Define -> defineCompiler.printFragment
			is FunctionCompiler.Parent.Body -> bodyCompiler.printFragment
		}.parent(begin("function"))

val DefineCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(
			module.definitions.printScript.plus(module.printScript))

val DefineCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is DefineCompiler.Parent.Body -> bodyCompiler.printFragment
			is DefineCompiler.Parent.Evaluator -> evaluatorNode.printFragment
		}.parent(begin("define"))

val Line.printScriptLine: ScriptLine
	get() =
		"type" lineTo script(scriptLine)

val Lines.printScript: Script
	get() =
		lineStack.map { printScriptLine }.script