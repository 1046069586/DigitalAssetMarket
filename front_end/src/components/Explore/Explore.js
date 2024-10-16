import { Col, Row, Pagination } from "antd";
import { Component } from "react";
import { Link } from "react-router-dom";

import ExploreCard from "./ExploreCard"
import request from '../../request'

class Explore extends Component {

    i = 0

    constructor(props) {
        super(props);
        this.state = {data: []}
        this.onChange = this.onChange.bind(this)
    };

    componentDidMount() {
        this.getData()
    };

    render() {
        return(
            <div>
                <header>
                    <h1>探索收藏品</h1>
                </header>
                <Row gutter={[0, 48]}>
                    {this.state.data.map((item, index) => (
                        <Col key={index} span={8}>
                        <Link to="/order" state={{data:item}}>
                            <ExploreCard title = {item.name}
                                        description = {item.description}
                                        url = {item.url }
                            />
                        </Link>
                    </Col>
                    ))}
                </Row>

                <Pagination defaultCurrent={1} onChange={this.onChange} total={20} />
            </div>
        )
    };

    getData() {
        request.get('/markets').then(res => {
            this.setState({data: res.data})
        })
    };

    onChange(page) {
        this.i = 0 + (page - 1) * 6
        this.forceUpdate()
    }
}

export default Explore