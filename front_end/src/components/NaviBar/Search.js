import { SearchOutlined} from '@ant-design/icons';
import { Input} from 'antd';

const { Search } = Input;

const prefix = (
  <SearchOutlined
    style={{
      fontSize: 16,
      color: '#1890ff',
    }}
  />
);

const SearchInput = () => {
    const onSearch = (value) => console.log(value);
    return <Search
            placeholder="输入商品"
            enterButton="搜索"
            size="large"
            prefix={prefix}
            onSearch={onSearch}
            />
}

export default SearchInput;
