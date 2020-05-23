package leo16.script

import leo13.Stack
import leo13.StackLink

val String.atom: Atom get() = StringAtom(this)
val Word.atom: Atom get() = WordAtom(this)
val StackLink<Letter>.word get() = Word(this)
val StackLink<Word>.spaced get() = Spaced(this)
val StackLink<Atom>.dotted get() = Dotted(this)
fun Spaced.sectionTo(script: Script) = Section(this, script)
val Dotted.block: Block get() = DottedBlock(this)
val Section.block: Block get() = SectionBlock(this)
val Stack<Block>.script get() = Script(this)
