import axios from 'axios'

const request = axios.create(
    {
        baseURL: "",
        timeout: 1000
    }
);
export default request;