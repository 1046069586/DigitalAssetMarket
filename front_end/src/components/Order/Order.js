import { Col, Row, Button, Image, message } from 'antd';
import { useLocation } from "react-router-dom";

import "./Order.css"
import request from "../../request"

function Order() {
    const {state:{data}} = useLocation()
    const buttonClick = () => {
        const username = JSON.parse(localStorage.getItem("user")).username;
        request.post("/order/" + username + "/" + data.id).then(res => {
            if(res.code === "200") {
                message.success("购买成功")
            }else {
                message.error("抱歉。购买失败！")
            }
        })
    }

    return(
        <div className='order'>
            <Row>
                <Col span={10}>
                    <Image 
                        width={450}
                        src={data.url}
                    />
                </Col>
                <Col span={11} offset={3} style={{paddingTop:'60px'}}>
                    <div className='name'>
                        {data.name}
                    </div>
                    <div className='priceTitle'>
                        现价: 
                    </div>
                    <div className='price'>
                        500
                    </div>
                    <div className='buttons'>
                        <Button type='primary' className='button' onClick={buttonClick}>购买</Button>
                    </div>
                </Col>
            </Row>
        </div>
    )
    
}

export default Order;