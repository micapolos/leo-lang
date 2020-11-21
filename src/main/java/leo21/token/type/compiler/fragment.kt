package leo21.token.type.compiler

import leo13.select
import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.emptyFragment
import leo14.fragment
import leo14.parent
import leo21.token.body.printFragment
import leo21.token.body.printFragmentParent
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
			is TypeNameTypeParent -> typeCompiler.printFragment.parent(begin(name))
			is ChoiceNameTypeParent -> choiceCompiler.printFragment.parent(begin(name))
			is ArrowDoingTypeParent -> arrowCompiler.printFragment.parent(begin("doing".typeKeyword))
			is ArrowNameTypeParent -> arrowCompiler.printFragment.parent(begin(name))
			is RecursiveTypeParent -> typeCompiler.printFragment.parent(begin("recursive".typeKeyword))
			is FunctionCompilerTypeParent -> functionCompiler.parentOrNull?.printFragmentParent
				?: emptyFragment.parent(begin("function".valueKeyword))
			is DefineCompilerTypeParent -> defineCompiler.printFragment.parent(begin("type".valueKeyword))
		}

val ChoiceCompiler.printFragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(choice.lineStack.script)

val ChoiceParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerChoiceParent -> typeCompiler.printFragment.parent(begin("choice".typeKeyword))
		}

val ArrowCompiler.printFragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(typeOrArrow.select(Type::script, Arrow::script))

val ArrowParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerArrowParent -> typeCompiler.printFragment.parent(begin("function".typeKeyword))
		}
