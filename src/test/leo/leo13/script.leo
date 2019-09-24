define
	letter
	contains: options: a b c d e f g h i j k l m n o p q r s t u v w x y z

	word
	contains: options
		letter
		link
			containing: word
			letter

	script
	contains: options
		empty
		link
			containing: script
			line
				word
				containing: script
