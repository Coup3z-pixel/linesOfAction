import { useEffect, useRef, useState } from "react"

function PlayerInfo({ time, username }: { time: string; username: string }) {
	return (
		<div id="opponent" className="h-16 border-2 flex gap-4 items-center px-5">
			<h6>{username}</h6>
			<h1 className="ml-auto">
				{time.indexOf(":") === time.length - 2
					? `${time.substring(0, time.indexOf(":"))}:0${time.substring(time.indexOf(":") + 1)}`
					: time}
			</h1>
		</div>
	)
}

function Board({ gameHistory }) {
	const [playerTime, setPlayerTime] = useState(300)
	const [opponentTime, setOpponentTime] = useState(300)
	const [isOpponentMove, setIsOpponentMove] = useState(false)
	const [isMovingPiece, setIsMovingPiece] = useState(false)
	const [currMovingPiece, setMovingPieceCord] = useState([-1, -1])
	const wsRef = useRef(null)

	const [board,setBoard] = useState([
		["", "x", "x", "x", "x", "x", "x", ""],
		["o", "", "", "", "", "", "", "o"],
		["o", "", "", "", "", "", "", "o"],
		["o", "", "", "", "", "", "", "o"],
		["o", "", "", "", "", "", "", "o"],
		["o", "", "", "", "", "", "", "o"],
		["o", "", "", "", "", "", "", "o"],
		["", "x", "x", "x", "x", "x", "x", ""],
	])

	function boardClick(rowIdx: number, colIdx: number) {
		console.log(rowIdx, colIdx)

		const [fromRow, fromCol] = currMovingPiece

		if(fromRow == rowIdx && fromCol == colIdx) return
		
		if (isMovingPiece && board[fromRow][fromCol] != "") {
			// Finish moving: attempt to move to clicked square
			const newBoard = board.map((row) => [...row]); // Deep clone
			newBoard[rowIdx][colIdx] = newBoard[fromRow][fromCol]; // Move piece
			newBoard[fromRow][fromCol] = ""; // Clear old position

			// Apply changes
			setMovingPieceCord([-1, -1]);
			setIsMovingPiece((prev) => !prev);
			setIsOpponentMove((prev) => !prev); // End your turn

			// Manually update board state if you plan to make it reactive
			// For now it's static, you would need useState if the board must update visually
			console.log("Moved piece to", rowIdx, colIdx);

			setBoard(newBoard)
		} else {
			setIsMovingPiece(true)
			setMovingPieceCord([rowIdx, colIdx])
		}
	}
	
	// Update timers with fresh state using functional updates
	useEffect(() => {
		const interval = setInterval(() => {
			if (isOpponentMove) {
				setOpponentTime((prev) => Math.max(prev - 1, 0))
			} else {
				setPlayerTime((prev) => Math.max(prev - 1, 0))
			}
		}, 1000)	

		return () => clearInterval(interval)
	}, [isOpponentMove]) // dependency triggers fresh interval setup if turn switches



	const formatTime = (time: number) => {
		const minutes = Math.floor(time / 60)
		const seconds = time % 60
		return `${minutes}:${seconds < 10 ? `0${seconds}` : seconds}`
	}

	return (
		<div id="game" className="flex flex-col gap-4">
			<PlayerInfo time={formatTime(opponentTime)} username="HI" />
			<div id="board" className="border-r-2 border-b-2">
				{board.map((row, rowIdx) => (
					<div className="flex" key={rowIdx}>
						{row.map((grid, colIdx) => (
							<div
								className={
									"w-12 h-12 aspect-square border-t-2 border-l-2 border-b-0 border-r-0 flex justify-center items-center" +
									((rowIdx * 9 + colIdx) % 2 === 0
										? " bg-green-500"
										: " bg-green-200") +
									(currMovingPiece[0] === rowIdx && currMovingPiece[1] === colIdx
										? " bg-yellow-500"
										: "")
								}
								onClick={() => boardClick(rowIdx, colIdx)}
								key={rowIdx * 8 + colIdx}
							>
								{grid}
							</div>
						))}
					</div>
				))}
			</div>
			<PlayerInfo time={formatTime(playerTime)} username="You" />
		</div>
	)
}

export default Board
