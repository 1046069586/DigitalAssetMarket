import { Card, Table } from 'antd';
import React, { useState, useEffect } from 'react';

const tabList = [
  {
    key: 'collect',
    tab: '收藏品',
  },
  {
    key: 'create',
    tab: '已发布',
  },
];


// {
//   collected: <p>content1</p>,
//   created: <p>content2</p>,
// };

const tableCol = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: '外部链接',
    dataIndex: 'externalLink',
    key: 'externalLink',
  },
  {
    title: '简介',
    dataIndex: 'description',
    key: 'description',
  },
];




const Cards = (props) => {
  
  const [activeTabKey, setActiveTabKey] = useState('collect');
  const [contentList, setContentList] = useState([]);

  useEffect(() => {
    if(activeTabKey === "collect") {
      setContentList(props.collect);
    } else {
      setContentList(props.create)
    }
  }, [activeTabKey, props.collect, props.create])

  const onTabChange = (key) => {
    setActiveTabKey(key);
  }


  return (
      <Card
        style={{
          width: '100%',
        }}
        tabList={tabList}
        activeTabKey={activeTabKey}
        onTabChange={(key) => {
          onTabChange(key);
        }}
      >
        <Table dataSource={contentList} columns={tableCol} />;
      </Card>
  )
}

export default Cards;