let gameHistoryElement;
let boardElement;
let gameHistory;
let moveIndex;

const gamePosition = {
	a2: 'black', a3: 'black', a4: 'black', a5: 'black', a6: 'black', a7: 'black',
	h2: 'black', h3: 'black', h4: 'black', h5: 'black', h6: 'black', h7: 'black',
	b1: 'white', c1: 'white', d1: 'white', e1: 'white', f1: 'white', g1: 'white', 
	b8: 'white', c8: 'white', d8: 'white', e8: 'white', f8: 'white', g8: 'white', 
}

document.addEventListener("DOMContentLoaded", () => {
	gameHistoryElement = document.getElementById("game-history");
	boardElement = document.getElementById("prev-game-display");

	gameHistory = gameHistoryElement.children;
	moveIndex = 0;
	
	// highlight first children
	gameHistory.item(0).classList.add("bg-orange-100");
	console.log(gameHistory.item(moveIndex))

	// addEventListener for each

	console.log(gameHistoryElement);
});

window.addEventListener('keydown', (event) => {
		switch(event.key) {
			case "ArrowLeft": // left
				if(moveIndex <= 0) return
				gameHistory.item(moveIndex).classList.remove("bg-orange-100");
				moveIndex--
				gameHistory.item(moveIndex).classList.add("bg-orange-100");

				let prevMove = gameHistory.item(moveIndex).innerText.split("->")
				console.log(prevMove)
				// reverse: move[1] -> move[0]
				// remove background from move[1]
				// add background to move[0]
				
				break
			case "ArrowRight": // right
				if(moveIndex >= gameHistory.length-1) return
				gameHistory.item(moveIndex).classList.remove("bg-orange-100");
				moveIndex++
				gameHistory.item(moveIndex).classList.add("bg-orange-100");

				let nextMove = gameHistory.item(moveIndex).innerText.split("->")
				console.log(nextMove)
				// forward: move[0] -> move[1]
				// remove background from move[0]
				// add background to move[1]

				break
			default:
				break
		}
	/*
		const prevSquare = document.querySelector(`[data-position="${moveBody.from}"]`);
		let pieceImg = prevSquare.style.backgroundImage;
		prevSquare.style.backgroundImage = null;

		const toSquare = document.querySelector(`[data-position="${moveBody.to}"]`);
		toSquare.style.backgroundImage = pieceImg;

		// TODO change gamePosition
		gamePosition[moveBody.to] = (playerIndex == 0 ? 'white' : 'black')
		gamePosition[moveBody.from] = undefined;
	*/

	});


