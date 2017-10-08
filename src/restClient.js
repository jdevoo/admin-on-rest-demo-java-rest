import addUploadFeature from './addUploadFeature';
import simpleRestClient from './SimpleRestClient';
import { fetchUtils } from 'admin-on-rest';

const httpClient = (url, options = {}) => {
    options.headers.set('Content-Range', 10);
    return fetchUtils.fetchJson(url, options);
};

const restClient = simpleRestClient('http://jsonplaceholder.typicode.com', httpClient);


const uploadCapableClient = addUploadFeature(restClient);
export default (type, resource, params) => new Promise(resolve => setTimeout(() => resolve(uploadCapableClient(type, resource, params)), 1000));