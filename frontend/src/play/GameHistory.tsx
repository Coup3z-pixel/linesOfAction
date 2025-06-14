function GameHistory({ opponent, gameHistory }) {
	return (
		<div id="move-history" className="flex-grow md:w-72 border-2 w-full flex flex-col">
			<div className="flex-grow grid-cols-3 grid">
			{
					gameHistory.map((move, index) => {
						<div className="items-center justify-center flex" key={index}>HI</div>
					})
			}
			</div>
			<div id="player-info" className="grid grid-cols-2 grid-rows-3">
				<div className="flex items-center justify-center border-t-2 border-r-2">
					<h1>Black</h1>
				</div>
				<div className="flex items-center justify-center border-t-2">
					<h1>White</h1>
				</div>

				<div className="flex items-center justify-center border-t-2 border-r-2">
					<h1>{opponent}</h1>
				</div>
				<div className="flex items-center justify-center border-t-2">
					<h1>You</h1>
				</div>
				<div className="flex items-center justify-center border-t-2 border-r-2">
					<h1 className="m-0">1000</h1>
				</div>
				<div className="flex items-center justify-center border-t-2">
					<h1 className="m-0">1000</h1>
				</div>
			</div>
		</div>
	)
}

export default GameHistory
