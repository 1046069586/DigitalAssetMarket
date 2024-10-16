import { Space, Button } from "antd"
import { Link } from "react-router-dom";

const HomeNavi = () => {
    return (
        <div>
            <p className="introduction">
                探索，收集，买卖 <br /><br />这些神奇的&nbsp;<strong>NFTs</strong>&nbsp;吧!
            </p>
            <Space size={[50]}>
                <Link to='/explore'><Button type="primary" style={{fontSize: "25px", blockSize:"auto"}}>购买</Button></Link>
                <Link to='/create'><Button type="default" style={{fontSize: "25px", blockSize:"auto"}}>发布</Button></Link>
            </Space>
        </div>
    )   
}

export default HomeNavi;