import './App.css';
import NaviBar from './components/NaviBar/NaviBar';
import Home from './components/Home/Home';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Explore from './components/Explore/Explore'
import Login from './components/Login/Login';
import Register from './components/Register/Register';
import RedirectRoute from './RedirectRoute';
import React from 'react';


function App() {

  return (
    <BrowserRouter>
    <div className="App">
      <div>
        <NaviBar />
      </div>
      <div>
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/explore' element={<Explore />} />
          <Route path='/login' element={<Login />} />
          <Route path='/register' element={<Register />} />
          <Route path='/*' element={<RedirectRoute />} />
        </Routes>
      </div>
    </div>
    </BrowserRouter>
  );
}

export default App;
