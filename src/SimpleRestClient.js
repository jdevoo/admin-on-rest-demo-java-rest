import {
    GET_LIST,
    GET_ONE,
    GET_MANY,
    GET_MANY_REFERENCE,
    CREATE,
    UPDATE,
    DELETE,
    fetchUtils
} from 'admin-on-rest';

import {customer, segment, command, product, category, review} from './entities';

const prepareGetListRequest = function(entity, params) {
    let req = (Object.keys(params.filter).length > 0) ?
        entity.getListWithFilterRequest(params) : entity.getListRequest(params);
    return {url: req.url, options: req.options}
}

const factory = function(resource) {
    switch(resource) {
        case "customers": return customer;
        case "segments": return segment;
        case "commands": return command;
        case "products": return product;
        case "categories": return category;
        case "reviews": return review;
        default: return null;
    }
}

export default (apiUrl, httpClient = fetchUtils.fetchJson) => {
    /**
     * @param {String} type One of the constants appearing at the top if this file, e.g. 'UPDATE'
     * @param {String} resource Name of the resource to fetch, e.g. 'posts'
     * @param {Object} params The REST request params, depending on the type
     * @returns {Object} { url, options } The HTTP request parameters
     */

    const convertRESTRequestToHTTP = (type, resource, params) => {
        let entity = factory(resource);
        switch (type) {
            case GET_LIST:
                return prepareGetListRequest(entity, params);

            case GET_ONE:
                return entity.getOneRequest(params.id);

            case GET_MANY:
                if (params.ids.length === 0) {
                    return {};
                }
                return entity.getManyRequest(params);

            case GET_MANY_REFERENCE:
                params.filter = {};
                params.filter[params.target] = params.id;
                return entity.getListWithFilterRequest(params);
            case UPDATE:
                return entity.editRequest(params.id, params.data);

            case CREATE:
                return entity.createRequest(params.data);

            case DELETE:
                return entity.deleteRequest(params.id);
            default:
                throw new Error(`Unsupported fetch action type ${type}`);
        }

    };

    /**
     * @param {Object} response HTTP response from fetch()
     * @param {String} type One of the constants appearing at the top if this file, e.g. 'UPDATE'
     * @param {String} resource Name of the resource to fetch, e.g. 'posts'
     * @param {Object} params The REST request params, depending on the type
     * @returns {Object} REST response
     */
    const convertHTTPResponseToREST = (response, type, resource, params) => {
        let entity = factory(resource);
        const {headers, json} = response;
        switch (type) {
            case GET_LIST:
            case GET_MANY_REFERENCE:
                return {
                    data: entity.getListResponse(json, params),
                    total: parseInt(
                        headers
                            .get('x-total-count')
                            .split('/')
                            .pop(),
                        10
                    ),
                };
            case CREATE:
                return entity.createResponse(params, json);
            case GET_ONE:
                return {data: entity.getOneResponse(json)};
            case UPDATE:
                return {data: entity.editResponse(json)};
            case GET_MANY:
                return {
                    data: entity.getListResponse(json, params)
                };
            case DELETE:
                return {
                    data: {}
                };
            default:
                throw new Error(`Unsupported response action type ${type}`);
        }
    };

    /**
     * @param {string} type Request type, e.g GET_LIST
     * @param {string} resource Resource name, e.g. "posts"
     * @param {Object} payload Request parameters. Depends on the request type
     * @returns {Promise} the Promise for a REST response
     */
    return (type, resource, params) => {
        const {url, options} = convertRESTRequestToHTTP(type, resource, params);
        if (url && options) {
            return httpClient(url, options)
                .then(response => convertHTTPResponseToREST(response, type, resource, params));
        }
        else {
            return Promise.resolve({data: []});
        }
    };
};
