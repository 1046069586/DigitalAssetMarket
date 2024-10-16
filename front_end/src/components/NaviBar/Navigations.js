import { UserOutlined, WalletOutlined, SmileTwoTone } from '@ant-design/icons';
import { Menu } from 'antd';
import { useState } from 'react';
import { Link, useNavigate } from "react-router-dom";



const Navigations = () => {
  const [current, setCurrent] = useState('mail');
  let navigate = useNavigate();

  const items = [
  {
    label: <Link to='/'>首页</Link>,
    key: 'home',
  },
  {
    label: '探索',
    key: 'explore',
    children: [
      {
        label: <Link to='/explore'>所有商品</Link>,
        key: 'all',
      },
      {
        label: '数字艺术品',
        key: 'art'
      },
      {
        label: '收藏品',
        key: 'collectible'
      },
    ],
  },
  {
    label: <Link to='/create'>发布</Link>,
    key: 'sell',
  },
  {
    label: '用户',
    key: 'user',
    icon: <UserOutlined />,
    children: [
      {
        label: <Link to='/profile'>登录</Link>,
        key: 'profile',
      },
      {
        label: '退出',
        key: 'quit',
        disabled: true
      },
    ]
  },
  {
    label: '钱包',
    key: 'wallet',
    icon: <WalletOutlined />
  },
];

  const onClick = (e) => {
    console.log('click ', e);
    setCurrent(e.key);
    if (e.key === 'quit') {
      navigate("/login");
      localStorage.removeItem("user")
      items[3].children[1].disabled = true
    }
  };

  
//判断用户是否已经登入，并做相应的页面变化
  if(localStorage.getItem("user")){
    items[3].icon = <SmileTwoTone />;
    items[3].label = JSON.parse(localStorage.getItem("user")).username
    items[3].children[0].label = <Link to='/profile'>个人信息</Link>
    items[3].children[1].disabled = false
  } 

  return <Menu onClick={onClick} 
                selectedKeys={[current]} 
                mode="horizontal" 
                items= {items}
                style={{fontStyle: 'italic', fontWeight: 'bold', fontSize: 16}}
          />;
};

export default Navigations;