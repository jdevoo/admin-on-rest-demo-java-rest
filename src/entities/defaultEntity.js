import {config} from '../config';
import format from 'string-format';
import APIUtils from  '../apiUtils';

export default class DefaultEntity {

    constructor(resourceName, embeddedKeys) {
        this.embeddedKeys = embeddedKeys;
        this.apiEndpoint = config.baseUrl + '/' + resourceName;
    }

    deleteRequest(id) {
        let url = format(this.apiEndpoint+'/{id}/published/false', {"id": id});
        let options = APIUtils.createOptionsForPOST({});
        return {url, options};
    }

    getListRequest(params) {
        params.filter = {};
        return this.getListWithFilterRequest(params);
    };

    getListResponse(response, params) {
        response.content = APIUtils.createDummyEntities(params, response.content);
        if(!response.content) {
            return [];
        }
        return APIUtils.prepareGetListResponse(response.content, this.embeddedKeys);
    }

    createRequest(data) {
        let postData = APIUtils.preparePostData(data);
        let url = this.apiEndpoint;
        let options = APIUtils.createOptionsForPOST(postData);
        return {url, options};
    }

    createResponse(params, response) {
        return {
            data: {...params.data, id: response.content[0].id}
        };
    }

    getOneRequest(id) {
        let url = format(this.apiEndpoint+'/{id}', {"id": id});
        let options = APIUtils.createOptionsForGET();
        return {url, options};
    }

    getOneResponse(response) {
        if(response.content.length === 0) {
            return {};
        }

        return APIUtils.prepareGetOneResponse(response.content[0],
            this.embeddedKeys);
    }

    editRequest(id, data) {
        let post_data = APIUtils.preparePostData(data);
        let url = format(this.apiEndpoint+'/{id}', {"id": id});
        const options = APIUtils.createOptionsForPUT(post_data);
        return {url, options};
    }

    editResponse(response) {
        return APIUtils.prepareEditResponse(response, this.embeddedKeys);
    }

    getListWithFilterRequest(params) {
        let filters = APIUtils.prepAuthorizeFilter(params.filter);
        let urLQuery = APIUtils.createUrlQuery(params);
        let url = this.apiEndpoint +"?"+ urLQuery;
        let options = APIUtils.createOptionsForGET();
        return {url, options};
    }

    getManyRequest(params) {
        return this.getListWithFilterRequest(params);
    }
}