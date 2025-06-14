import { useState } from "react";
import Board from "./Board";
import GameHistory from "./GameHistory";

function Play() {
	const [gameHistory, setGameHistory] = useState(["e4", "e5"])

	return (
		<main className="flex justify-center items-center w-screen h-screen">
			<div className="flex flex-col gap-4 md:flex-row">
				<Board gameHistory={gameHistory}/>
				<GameHistory opponent={"HI"} gameHistory={gameHistory}/>
			</div>
		</main>
	)
}

export default Play
