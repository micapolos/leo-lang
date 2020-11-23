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
import leo14.script
import leo21.token.define.DefineCompiler
import leo21.token.evaluator.printFragment
import leo21.token.strings.typeKeyword
import leo21.token.strings.valueKeyword
import leo21.token.type.compiler.Lines
import leo21.type.Line
import leo21.type.nameOrNull
import leo21.type.printScript
import leo21.type.scriptLine

val Body.printScript: Script
	get() =
		compiled.type.printScript

val BodyCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(body.printScript)

val BodyCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is BodyCompiler.Parent.BodyName ->
				bodyCompiler.printFragment.parent(begin(name))
			is BodyCompiler.Parent.BodyDo ->
				bodyCompiler.printFragment.parent(begin("do".typeKeyword))
			is BodyCompiler.Parent.BodyRepeat ->
				bodyCompiler.printFragment.parent(begin("repeat".typeKeyword))
			is BodyCompiler.Parent.BodyApply ->
				bodyCompiler.printFragment.parent(begin("apply".typeKeyword))
			is BodyCompiler.Parent.FunctionDoes ->
				functionCompiler.printFragment.parent(begin("does".typeKeyword))
			is BodyCompiler.Parent.SwitchCase ->
				switchCompiler.printFragment.parent(begin(case.nameOrNull!!))
			is BodyCompiler.Parent.EvaluatorRepeat ->
				evaluatorNode.printFragment.parent(begin("repeat".valueKeyword))
		}

val SwitchCompiler.printFragment: Fragment
	get() =
		parentBodyCompiler.printFragment
			.parent(begin("switch".valueKeyword))
			.fragment(caseFieldStack.map { scriptLine }.script)

val FunctionCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(type.printScript)

val FunctionDoesCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(arrowCompiled.arrow.printScript)

val FunctionCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is FunctionCompiler.Parent.Define -> defineCompiler.printFragment
			is FunctionCompiler.Parent.Body -> bodyCompiler.printFragment
			is FunctionCompiler.Parent.Evaluator -> evaluatorNode.printFragment
		}.parent(begin("function".typeKeyword))

val DefineCompiler.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(module.printScript)

val DefineCompiler.Parent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is DefineCompiler.Parent.Body -> bodyCompiler.printFragment
			is DefineCompiler.Parent.Evaluator -> evaluatorNode.printFragment
		}.parent(begin("define".typeKeyword))

val Line.printScriptLine: ScriptLine
	get() =
		"type".typeKeyword lineTo script(scriptLine)

val Lines.printScript: Script
	get() =
		lineStack.map { printScriptLine }.script

val RepeatCompiler.printFragment: Fragment
	get() =
		parent.printFragmentParent.fragment(script())

val RepeatParent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is BodyCompilerRepeatParent -> bodyCompiler.printFragment.parent(begin("repeat".typeKeyword))
			is EvaluatorNodeRepeatParent -> evaluatorNode.printFragment.parent(begin("repeat".valueKeyword))
		}