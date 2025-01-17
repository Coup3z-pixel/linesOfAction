let gameContainer;
let playerStatus;
let oppenentStatus;
let linesOfAction;
let gameMenu;

const privatePreUrl = '/player/'
const gameStartUrl = '/move/start'
const moveOnlineUrl = '/move/online'

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

stompClient.onConnect = (frame) => {
			console.log('Connected: ' + frame);

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

			stompClient.subscribe(privatePreUrl + playerId + moveOnlineUrl, (move) => {
				let moveBody = JSON.parse(move.body);

				const fromSquare = document.querySelector(`[data-position="${moveBody.from}"]`);
				let pieceImg = fromSquare.style.backgroundImage;
				fromSquare.style.backgroundImage = '';

				const toSquare = document.querySelector(`[data-position="${moveBody.to}"]`);
				toSquare.style.backgroundImage = pieceImg;
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

	

	linesOfAction.addEventListener('click', (e) => {
		const square = e.target;

		// Select a square
		if (!selectedSquare && square.style.backgroundImage) {
			selectedSquare = square;
			square.classList.add('bg-yellow-600');
		} 
		// Move the piece
		else if (selectedSquare) {

			stompClient.publish({
				destination: "/play/online",
				body: JSON.stringify({
					gameId,
					from: selectedSquare.dataset.position,
					to: square.dataset.position,
					playerIndex: playerIndex
				})
			})

			pieceImg = selectedSquare.style.backgroundImage;

			selectedSquare.style.backgroundImage = null;
			selectedSquare.textContent = '';
			selectedSquare.classList.remove('bg-yellow-600');
			square.style.backgroundImage = pieceImg;
			selectedSquare = null	;


			gamePosition[square.dataset.position] = playerIndex;
		}
	});

	gameContainer.insertBefore(linesOfAction, playerStatus)

	for (const position in gamePosition) {
		const square = document.querySelector(`[data-position="${position}"]`);
		square.style.backgroundImage = `url('/${gamePosition[position]}-checker.svg')`;
	}
})
