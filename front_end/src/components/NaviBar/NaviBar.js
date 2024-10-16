import Navigations from './Navigations';
import SearchInput from './Search';
import { Col, Row } from 'antd';
import React from 'react';

class NaviBar extends React.Component {
    render() {
        return(
            <div>
            <Row align='bottom'>
                <Col span={4} style={{fontStyle: 'italic', fontSize: 20, fontWeight: 'bold'}}>
                    <img src='logo512.png' alt='NFT 交易市场' width="50" height="50"/>
                    NFT 交易市场
                </Col>
                <Col span={10} offset={1}><SearchInput /></Col>
                <Col span={7} offset={1}><Navigations /></Col>
            </Row>
            </div>
        );
    }
}

export default NaviBar;
