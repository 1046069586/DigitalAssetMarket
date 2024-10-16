import { Route, Navigate, Routes } from 'react-router-dom';

import Profile from './components/Profile/Profile';
import Order from './components/Order/Order';
import Create from './components/Create/Create';

const RedirectRoute = () => {
    // const { element: Component, path } = props;
    const user = localStorage.getItem("user");

    if (user) {
        return (
            <Routes>
            <Route path = "/profile" element={<Profile />} />
            <Route path = "/order" element={<Order />} />
            <Route path='/create' element={<Create />} />
            </Routes>
        );
    } else {
        return (<Navigate  to='/login' />)
    }
}

// class RedirectRoute extends Route {
//     render() {
//         const user = localStorage.getItem("user");
//         if (user) {
//             return (<Route path = "/profile" element={<Profile />} />);
//         } else {
//             return <Navigate  to='/login' />
//         }
//     }
// }

export default RedirectRoute