package leo21.token.type.compiler

import leo13.select
import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.emptyFragment
import leo14.fragment
import leo14.parent
import leo14.script
import leo21.token.body.printFragment
import leo21.token.body.printFragmentParent
import leo21.token.strings.type
import leo21.token.strings.typeKeyword
import leo21.token.strings.valueKeyword
import leo21.type.Arrow
import leo21.type.Type
import leo21.type.printScript
import leo21.type.script

val TypeCompiler.printFragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(type.printScript)

val TypeParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeNameTypeParent -> typeCompiler.printFragment.parent(begin(name.type))
			is ChoiceNameTypeParent -> choiceCompiler.printFragment.parent(begin(name.type))
			is ArrowDoingTypeParent -> arrowCompiler.printFragment.parent(begin("doing".typeKeyword))
			is ArrowNameTypeParent -> arrowCompiler.printFragment.parent(begin(name.type))
			is RecursiveTypeParent -> typeCompiler.printFragment.parent(begin("recursive".typeKeyword))
			is FunctionCompilerTypeParent -> functionCompiler.parentOrNull?.printFragmentParent
				?: emptyFragment.parent(begin("function".valueKeyword))
			is FunctionToCompilerTypeParent -> functionCompiler.printFragment.parent(begin("to".typeKeyword))
			is DefineCompilerTypeParent -> defineCompiler.printFragment.parent(begin("type".typeKeyword))
			is RepeatDoingCompilerTypeParent -> repeatCompiler.printFragment.parent(begin("doing".typeKeyword))
		}

val ChoiceCompiler.printFragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(choice.lineStack.printScript)

val ChoiceParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerChoiceParent -> typeCompiler.printFragment.parent(begin("choice".typeKeyword))
		}

val ArrowCompiler.printFragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(typeOrArrow.select(Type::printScript, Arrow::printScript))

val ArrowParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerArrowParent -> typeCompiler.printFragment.parent(begin("function".typeKeyword))
		}

val TypeRecurseCompiler.printFragment: Fragment
	get() =
		parentTypeCompiler.printFragment.parent(begin("recurse".typeKeyword)).fragment(script())