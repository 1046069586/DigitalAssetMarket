import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input, Space, message } from 'antd';
import React  from 'react';
import { useNavigate, Link } from "react-router-dom";
import "./Login.css"
import request from "../../request"

const Login = () => {

  let navigate = useNavigate();

  const onFinish = (values) => {
    request.post("/users/login", values).then(res => {
      if (res.code === "200") {
        localStorage.setItem("user", JSON.stringify(res.data) )
        navigate("/");
      } else {
        message.error("用户名或密码错误！")
      }
    })
  };

  return (
    <div>
    <h1 className='loginheader'>用户登录</h1>
    <Form
      name="normal_login"
      className="login-form"
      initialValues={{
        remember: true,
      }}
      onFinish={onFinish}
    >
      <Form.Item
        name="username"
        rules={[
          {
            required: true,
            message: '请输入用户名!',
          },
        ]}
      >
        <Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="用户名" />
      </Form.Item>
      <Form.Item
        name="password"
        rules={[
          {
            required: true,
            message: '请输入密码!',
          },
        ]}
      >
        <Input
          prefix={<LockOutlined className="site-form-item-icon" />}
          type="password"
          placeholder="密码"
        />
      </Form.Item>

      <Form.Item className='rememberItem'>
        <Space size={300}>
            <Form.Item name="remember" valuePropName="checked" noStyle>
            <Checkbox>记住我</Checkbox>
            </Form.Item>

            <a className="login-form-forgot" href="/">
            忘记密码?
            </a>
        </Space>
      </Form.Item>

      <Form.Item >
            <Button type="primary" htmlType="submit" className="login-form-button">
                登录
            </Button>
            <div className='registerLink'>
              还没有账号？ <Link to='/register'>点击注册!</Link>
            </div>  
        
      </Form.Item>
    </Form>
    </div>
  );
};

export default Login;