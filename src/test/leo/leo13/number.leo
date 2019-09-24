define
	natural: number
	contains: options
		zero
		next: natural

	natural: number
	plus: natural: number: zero
	gives: given natural

	natural: number
	plus: next: natural: number
	gives
		next: given natural
		plus: given next natural
