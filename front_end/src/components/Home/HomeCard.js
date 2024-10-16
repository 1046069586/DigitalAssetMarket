import { Card } from 'antd';
const { Meta } = Card;

const HomeCard = () => {
  return (
  <Card
    hoverable
    style={{
      width: 360,
    }}
    cover={<img alt="card" src="card.png"/>}
  >
    <Meta title="Unstoppable Love" description="created by stefanolezcano" />
  </Card>)
};

export default HomeCard;