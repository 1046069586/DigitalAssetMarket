import { Component } from "react";
import { UserOutlined } from '@ant-design/icons';
import { Avatar } from 'antd';
import Cards from "./Cards"
import "./Profile.css"
import request from "../../request"

class Profile extends Component {
    

    constructor(props) {
        super(props);
        this.user = JSON.parse(localStorage.getItem("user"));
        this.username = this.user.username;
        this.state = {collectData : [], createData : []}
    };

    componentDidMount(){
        request.get("/users/create/" + this.username).then(res => {
            for(var i=0; i<res.data.length; i++) {
                res.data[i].key = i+1
            }
            this.setState({createData : res.data})
        });
        request.get("/users/collect/" + this.username).then(res => {
            for(var i=0; i<res.data.length; i++) {
                res.data[i].key = i+1
            }
            this.setState({collectData : res.data})
        });

        
    }
   

    render() {
        return (
            <div>
                <div className="avatar">
                    <Avatar size={128}  style={{ backgroundColor: '#87d068' }} icon={<UserOutlined />} />
                </div>
                <div className="username">
                    {this.username}
                </div>
                <div className="cards">
                    <Cards collect={this.state.collectData} create={this.state.createData}/>
                </div>
                
            </div>
        )
    }
}

export default Profile;