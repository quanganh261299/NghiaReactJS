import * as React from "react";
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import { route } from './routes/route'
import './App.css'


function App() {
  const router = createBrowserRouter(route);
  return (
    <>
      <RouterProvider router={router} />
    </>
  )
}

export default App
