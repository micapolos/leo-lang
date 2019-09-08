package leo13

object ObjectSentence

val sentence = ObjectSentence

val ObjectSentence.sentence: Sentence
	get() =
		sentence(sentenceWord)