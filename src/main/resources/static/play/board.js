let gameContainer;
let playerStatus;
let oppenentStatus;
let linesOfAction;
let gameMenu;
let playerCount;

const publicPreUrl = '/lobby'
const privatePreUrl = '/player/'
const gameStartUrl = '/move/start'
const moveOnlineUrl = '/move/online'


const gamePosition = {
	a2: 'black', a3: 'black', a4: 'black', a5: 'black', a6: 'black', a7: 'black',
	h2: 'black', h3: 'black', h4: 'black', h5: 'black', h6: 'black', h7: 'black',
	b1: 'white', c1: 'white', d1: 'white', e1: 'white', f1: 'white', g1: 'white', 
	b8: 'white', c8: 'white', d8: 'white', e8: 'white', f8: 'white', g8: 'white', 
}

function uuidv4() {
	return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
	.replace(/[xy]/g, function (c) {
			const r = Math.random() * 16 | 0, 
					v = c == 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
	});
}

const playerId = uuidv4();
let playerIndex = -1;

const stompClient = new StompJs.Client({
	brokerURL: 'ws://localhost:8080/play-websocket'
});

let gameId = 0;

let prevMove = {
	to: '', 
	from: '', 
	toSquare: '',
}

let fromSquare = null;

const handleBoardClick = (e) => {
	const toSquare = e.target;

	// Select a toSquare
	if (!fromSquare && toSquare.style.backgroundImage) {
		fromSquare = toSquare;
		toSquare.classList.add('bg-yellow-600');
	} 
	// Move the piece
	else if (fromSquare) {

		stompClient.publish({
			destination: "/play/online",
			body: JSON.stringify({
				gameId,
				from: fromSquare.dataset.position,
				to: toSquare.dataset.position,
				playerIndex: playerIndex
			})
		})

		prevMove = {
			from: fromSquare.dataset.position,
			to: toSquare.dataset.position,
			toSquare: toSquare.style.backgroundImage
		}

		let pieceImg = fromSquare.style.backgroundImage
		fromSquare.style.backgroundImage = null;
		fromSquare.textContent = '';
		fromSquare.classList.remove('bg-yellow-600');
		toSquare.style.backgroundImage = pieceImg;
		fromSquare = null	;
	}
};

stompClient.onConnect = (frame) => {
	console.log('Connected: ' + frame);

	// Listens to number of players
	stompClient.subscribe('/move/lobby', (status) => {
		playerCount.innerHTML = JSON.parse(status.body).count
	});

	// Listens to game Start
	stompClient.subscribe(privatePreUrl + playerId + gameStartUrl, (status) => {
		let gameStatus = JSON.parse(status.body);
		if(gameStatus.status == "Starting") {
			// Change Menu for the moves
			gameMenu.innerHTML = ''

			gameMenu.classList.add('h-full')
			gameMenu.classList.add('bg-stone-400')

			// Change the Player Status with the valid details

			// Change the subscription
			playerIndex = gameStatus.playerIndex;
			gameId = gameStatus.gameId;
		} else {
			console.log(gameStatus)
		}
	});

	
	linesOfAction.addEventListener('click', handleBoardClick);

	// Listens on live moves
	stompClient.subscribe(privatePreUrl + playerId + moveOnlineUrl, (move) => {

		let moveBody = JSON.parse(move.body);

		// If move is not valid
		if(moveBody == false) {
			// Reverse the move
			const toSquare = document.querySelector(`[data-position="${prevMove.to}"]`);
			let pieceImg = toSquare.style.backgroundImage;
			toSquare.style.backgroundImage = prevMove.toSquare;
			const prevSquare = document.querySelector(`[data-position="${prevMove.from}"]`);
			prevSquare.style.backgroundImage = pieceImg;

			return;
		}

		// If game is over
		if(moveBody.status) {
			console.log("Congrats, you may suck");
			alert(`You have ${moveBody.status}`);
			linesOfAction.removeEventListener("click", handleBoardClick);

			return; 
		}

		// If move is valid
		const prevSquare = document.querySelector(`[data-position="${moveBody.from}"]`);
		let pieceImg = prevSquare.style.backgroundImage;
		prevSquare.style.backgroundImage = null;

		const toSquare = document.querySelector(`[data-position="${moveBody.to}"]`);
		toSquare.style.backgroundImage = pieceImg;

		// TODO change gamePosition
		gamePosition[moveBody.to] = (playerIndex == 0 ? 'white' : 'black')
		gamePosition[moveBody.from] = undefined;

		return;
	})
};

stompClient.onWebSocketError = (error) => {
	console.error('Error with websocket', error);
	disconnect();
};

stompClient.onStompError = (frame) => {
		console.error('Broker reported error: ' + frame.headers['message']);
		console.error('Additional details: ' + frame.body);
};

function getMatch() {
	stompClient.publish({
		destination: `/play/start`,
		body: JSON.stringify({
			id: playerId
		})
	});
}

function disconnect() {
		stompClient.deactivate();
		console.log("Disconnected");
}

stompClient.activate();


document.addEventListener("DOMContentLoaded", () => {
	gameContainer = document.getElementById("game-container")
	playerStatus = document.getElementById("player-stats")
	oppenentStatus = document.getElementById("opponent-stats")
	linesOfAction = document.createElement("lines-of-action")
	gameMenu = document.getElementById("game-menu")
	playerCount = document.getElementById("player-waiting-no")

	linesOfAction.classList.add('aspect-square')
	linesOfAction.classList.add('border-2')
	linesOfAction.classList.add('border-black')
	linesOfAction.classList.add('grid')
	linesOfAction.classList.add('grid-rows-8')
	linesOfAction.classList.add('grid-cols-8')
	linesOfAction.classList.add('h-2/3')

	const letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'];

	for (let row = 8; row >= 1; row--) {
		for (let col = 0; col < 8; col++) {
			const square = document.createElement('div');
			square.classList.add('w-full');
			square.classList.add('flex-grow');
			square.classList.add('flex');
			square.classList.add('items-center');
			square.classList.add('justify-center');
			square.classList.add('bg-contain');
			square.classList.add('bg-no-repeat');
			square.classList.add('bg-center');

			square.classList.add((row + col) % 2 === 0 ? 'bg-orange-100' : 'bg-orange-900');
			square.dataset.position = `${letters[col]}${row}`; // e.g., a1, b1, etc.
			linesOfAction.appendChild(square);
		}
	}

	gameContainer.insertBefore(linesOfAction, playerStatus)

	for (const position in gamePosition) {
		const square = document.querySelector(`[data-position="${position}"]`);
		square.style.backgroundImage = `url('/${gamePosition[position]}-checker.svg')`;
	}
})
