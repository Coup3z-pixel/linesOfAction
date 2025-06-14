import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { useState } from 'react'
import Play from './play/Play'
import Index from "./Index";
import History from './history/History'
import Setting from './settings/Settings'
import Join from "./join/Join";

function Router() {
  return (
    <>
		<BrowserRouter>
			<Routes>
				<Route path="/">
					<Route index element={<Index />}/>
					<Route path="join" element={<Join />}/>
					<Route path="play" element={<Play />}/>
					<Route path="history" element={<History />}/>
					<Route path="setting" element={<Setting />}/>
				</Route>
			</Routes>
		</BrowserRouter>
    </>
  )
}

export default Router
