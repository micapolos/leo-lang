package leo16.script

import leo13.Stack
import leo13.StackLink

data class Script(val blockStack: Stack<Block>)

sealed class Block
data class DottedBlock(val dotted: Dotted) : Block()
data class SectionBlock(val section: Section) : Block()

data class Section(val spaced: Spaced, val script: Script)

data class Dotted(val atomStackLink: StackLink<Atom>)
data class Spaced(val wordStackLink: StackLink<Word>)

data class Word(val letterStackLink: StackLink<Letter>)
enum class Letter {
	A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
}

sealed class Atom
data class StringAtom(val string: String) : Atom()
data class WordAtom(val word: Word) : Atom()