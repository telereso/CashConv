import logo from './logo.svg';
import './App.css';
import React, { useState ,useEffect} from 'react';
const CashconvClient = require('@telereso/cashconv-client').io.telereso.cashconv.client

const manger = new CashconvClient.CashconvClientManager.Builder().build()
window.manger = manger


function App() {
    const [text, setText] = useState("Loading....");

    useEffect(() => {
        manger.fetchRates()
            .onSuccess((e) => {
                console.log("onSuccess",e)
            })
            .onSuccessUI((data) => {
                setText(`Rates: ${data}`)
            })
            .onFailure((e) => {
                console.log(e)
            })
            .onFailureUI((e) => {
                setText(e.message)
            })
    },[]);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <p>
                    {text}
                </p>
                <a
                    className="App-link"
                    href="https://reactjs.org"
                    target="_blank"
                    rel="noopener noreferrer"
                >
                    Learn React
                </a>
            </header>
        </div>
    );
}

export default App;
