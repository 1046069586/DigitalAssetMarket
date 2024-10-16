import { Component } from "react";
import FileUpload from "./FileUpload";
import Forms from "./Forms";
import './Create.css'

class Create extends Component {
    render() {
        return(
            <div className="create">
                <header className="head">
                    <h1>铸造自己的NFT</h1>
                </header>
                <div className="upload">
                    <FileUpload />
                </div>
                
                <Forms />

            </div>
        )
    }
}

export default Create;