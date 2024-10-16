import { Col, Row } from 'antd';
import React from 'react';
import HomeCard from './HomeCard';
import HomeNavi from './HomeNavi';
import './Home.css'

class Home extends React.Component{
    render() {
        return(
            <div className='home'>
                <Row align='middle'>
                    
                    <Col span={12}>
                        <Row>
                            <Col span={24}></Col>
                            <Col span={24}>
                                <HomeNavi />
                            </Col>
                        </Row>
                    </Col>

                    <Col span={8} offset={4}>
                        <HomeCard />
                    </Col>

                </Row>
            </div>
        )
    }
}

export default Home;