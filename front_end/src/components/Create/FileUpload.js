import { FileAddOutlined } from '@ant-design/icons';
import { message, Upload } from 'antd';

const { Dragger } = Upload;

const props = {
  name: 'file',
  multiple: true,
  action: 'http://192.168.233.128:9090/files',

  onChange(info) {
    // 在上传过程会多次调用，根据status值确定上传状态
    const { status } = info.file;

    if (status === 'done') {
      message.success(`${info.file.name} file uploaded successfully.`);
      var fileData = info.file.response.data;
      localStorage.setItem("fileData", JSON.stringify(fileData))
    } else if (status === 'error') {
      message.error(`${info.file.name} file upload failed.`);
    }
  },

  onDrop(e) {
    console.log('Dropped files', e.dataTransfer.files);
  },
};

const FileUpload = () => (
  <Dragger {...props} height='250px'>
    <p className="ant-upload-drag-icon">
      <FileAddOutlined /> 
    </p>
    <p className="ant-upload-text">点击或拖拽上传文件</p>
    <p className="ant-upload-hint">
      文件格式支持JPG, PNG
    </p>
  </Dragger>
);

export default FileUpload;