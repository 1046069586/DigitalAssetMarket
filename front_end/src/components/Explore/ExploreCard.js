import { Card } from 'antd';
const { Meta } = Card;

const ExploreCard = (props) => {
  return (
  <Card
    style={{
      width: 360
    }}
    cover={
    <img alt="card" src={props.url} height='360'/>
    }
  >
    <Meta title= {props.title} description={props.description} />
  </Card>)
};

export default ExploreCard;