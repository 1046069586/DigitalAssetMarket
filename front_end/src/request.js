import axios from 'axios';
import { message } from 'antd';

const request = axios.create({
    baseURL: 'http://192.168.233.128:9090',
})

//统一设置请求头
request.interceptors.request.use(config => {
    let user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {token:''}
    if (user){
        config.headers["token"] = user.token
    }
    return config;
    }, 
    error => {
        return Promise.reject(error)
    });


// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res =response.data 
        
        if(res.code === "500"){
            message.error("系统错误！")
            window.location.replace("http://localhost:3000");  //go(-1);
        }

        if(res.code === "400"){
            window.location.replace("http://localhost:3000/login")  ;
            message.error("请先登录！")
        }
        
        // // 如果是返回的文件
        // if (response.config.responseType === 'blob') {
        //     return res
        // }
        // 兼容服务端返回的字符串数据
        // if (typeof res === 'string') {
        //     res = res ? JSON.parse(res) : res
        // }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)

export default request