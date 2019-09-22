define
	letter has: options: a b c d e f g h i j k l m n o p q r s t u v w x y z
	word has: options
		letter
		link: itself letter
	script has: options
		empty
		link
			itself
			line: word itself
