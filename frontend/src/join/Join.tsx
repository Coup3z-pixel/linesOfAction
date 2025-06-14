import { useEffect, useRef, useState } from 'react';
import SockJS from "sockjs-client";
import { Client, Stomp } from "@stomp/stompjs";
import { Navigate, useNavigate } from 'react-router-dom';

function Join() {

	function generateUUID() {
		return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
			const r = Math.random() * 16 | 0;
			const v = c === 'x' ? r : (r & 0x3 | 0x8);
			return v.toString(16);
		});
	}

	const stompClientRef = useRef(null);
	const navigate = useNavigate()
	const userId = generateUUID()

	
	useEffect(() => {
		const socket = new SockJS("http://localhost:8080/game-play");
		const stompClient = new Client({
			webSocketFactory: () => socket,
			reconnectDelay: 5000,
			connectHeaders: {
				userId: userId
			},
			onConnect: () => {
				console.log("Connected");

				stompClient.subscribe(`/user/match-info/connect`, (message) => {
					const response = JSON.parse(message.body);
					console.log("Matchmaking response:", response);
					setPlayerCount(response.playersWaiting);

					if (response.found) {
						navigate('/play', { replace: true })
					}
				});

				stompClient.subscribe("/match-info/queue-size", (message) => {
					const response = JSON.parse(message.body)
					setPlayerCount(response.playersWaiting)
				})

				stompClient.publish({
					destination: "/match-action/ask",
					body: JSON.stringify({}),
				});
			},
			onStompError: (frame) => {
				console.error("Broker error:", frame.headers["message"]);
			}
		});

		stompClient.activate()
		stompClientRef.current = stompClient


		return () => {
			stompClient.deactivate()
		}
	}, [])


	let [isInQueue, setIsInQueue] = useState(false)
	let [playerCount, setPlayerCount] = useState(-1)

	function joinQueue() {
		const stompClient = stompClientRef.current

		if(isInQueue || (!stompClient || !stompClient.connected)) return


		const payload = {
			recipient: userId,
			userId: "", // You can use state or props here
			elo: 1200,
		};

		stompClient.publish({
			destination: "/match-action/find",
			body: JSON.stringify(payload),
		});

		console.log("Joined Queue")
		setIsInQueue(true)
	}


	return (
		<>
			<main className="flex flex-col justify-center items-center h-screen w-screen gap-4">
				<h1>Players in Queue: {playerCount == -1 ? "" : playerCount}</h1>
				<button 
					className="rounded-full border-2 px-4 py-2" 
					onClick={joinQueue}
				>{isInQueue
					? <div className='flex'><svg width="24" height="24" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path d="M12,1A11,11,0,1,0,23,12,11,11,0,0,0,12,1Zm0,20a9,9,0,1,1,9-9A9,9,0,0,1,12,21Z" transform="translate(12, 12) scale(0)"><animateTransform id="spinner_XR07" begin="0;spinner_npiH.begin+0.4s" attributeName="transform" calcMode="spline" type="translate" dur="1.2s" values="12 12;0 0" keySplines=".52,.6,.25,.99"/><animateTransform begin="0;spinner_npiH.begin+0.4s" attributeName="transform" calcMode="spline" additive="sum" type="scale" dur="1.2s" values="0;1" keySplines=".52,.6,.25,.99"/><animate begin="0;spinner_npiH.begin+0.4s" attributeName="opacity" calcMode="spline" dur="1.2s" values="1;0" keySplines=".52,.6,.25,.99"/></path><path d="M12,1A11,11,0,1,0,23,12,11,11,0,0,0,12,1Zm0,20a9,9,0,1,1,9-9A9,9,0,0,1,12,21Z" transform="translate(12, 12) scale(0)"><animateTransform id="spinner_r5ci" begin="spinner_XR07.begin+0.4s" attributeName="transform" calcMode="spline" type="translate" dur="1.2s" values="12 12;0 0" keySplines=".52,.6,.25,.99"/><animateTransform begin="spinner_XR07.begin+0.4s" attributeName="transform" calcMode="spline" additive="sum" type="scale" dur="1.2s" values="0;1" keySplines=".52,.6,.25,.99"/><animate begin="spinner_XR07.begin+0.4s" attributeName="opacity" calcMode="spline" dur="1.2s" values="1;0" keySplines=".52,.6,.25,.99"/></path><path d="M12,1A11,11,0,1,0,23,12,11,11,0,0,0,12,1Zm0,20a9,9,0,1,1,9-9A9,9,0,0,1,12,21Z" transform="translate(12, 12) scale(0)"><animateTransform id="spinner_npiH" begin="spinner_XR07.begin+0.8s" attributeName="transform" calcMode="spline" type="translate" dur="1.2s" values="12 12;0 0" keySplines=".52,.6,.25,.99"/><animateTransform begin="spinner_XR07.begin+0.8s" attributeName="transform" calcMode="spline" additive="sum" type="scale" dur="1.2s" values="0;1" keySplines=".52,.6,.25,.99"/><animate begin="spinner_XR07.begin+0.8s" attributeName="opacity" calcMode="spline" dur="1.2s" values="1;0" keySplines=".52,.6,.25,.99"/></path></svg><span>Join The Queue</span></div>
					: "Join the Queue"}</button>
			</main>
			
		</>
	)
}

export default Join
