import format from 'string-format';
import { stringify } from 'query-string';
export default class APIUtils {

    static guid() {
      let s4 = function() {
        return Math.floor((1 + Math.random()) * 0x10000)
          .toString(16)
          .substring(1);
      }
      return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
    }

    /***** Request utils *****/

    static createDummyEntities(params, results) {
        if (!params.hasOwnProperty("ids")) {
            return results;
        }
        let ids = params.ids;
        for (let i = 0; i<ids.length; i++) {
            ids[i] = ids[i] + "";
        }

        let idsFetched = [];
        for (let j=0; j<results.length; j++){
            let resId = 0;
            if (results[j].hasOwnProperty("id")) {
                resId = results[j].id + "";
            }
            idsFetched.push(resId);
        }

        for (let i=0; i<ids.length; i++) {
            if (idsFetched.indexOf(ids[i]) === -1) {
                results.push({id: ids[i]});
            }

        }
        return results;
    }

    static snakeToCamelCase(snakeString) {
    	return snakeString.replace(/_([a-z])/g, function (g) { return g[1].toUpperCase(); });
    }

    static camelToSnakeCase(camelString) {
    	return camelString.replace(/([a-z][A-Z])/g, function (g) { return g[0] + '-' + g[1].toLowerCase() });
    }

    /***** Response utils *****/
    static prepareGetListResponse(rows, embeddedKeys) {

        return rows.map(function (row) {
            return APIUtils.oneRowResponseHelper(row, embeddedKeys, true);
        });
    }

    static prepareGetOneResponse(row, embeddedKeys) {
        return APIUtils.oneRowResponseHelper(row, embeddedKeys, false);
    }

    static prepareEditResponse(row, embeddedKeys) {
        return APIUtils.oneRowResponseHelper(row, embeddedKeys, false);
    }

    static preparePostData(data) {
        return JSON.stringify(data);
    }

    static oneRowResponseHelper(row, embeddedKeys, rowInList) {
        let result = {};

        for (let [key,val] of Object.entries(row)) {
            let valIsNumber = Number.isInteger(val);
            let valIsEmbedded = embeddedKeys.indexOf(key) > -1;
            let valIsArray = Array.isArray(val);
            let valIsEmptyArray = valIsArray && val.length === 0;
            let valIsObjectArray = valIsArray && val.length > 0 && val[0].hasOwnProperty("id");
            let valIsNumberArray = valIsArray && val.length > 0 && Number.isInteger(val[0]);
            let valIsObject = val !== null && val.hasOwnProperty("id") && !valIsArray && !valIsNumber;

            if(valIsEmbedded && !rowInList) {
                result[key] = val;
            }
            else if (valIsEmptyArray) {
                result[key] = val;
            }
            else if (valIsObjectArray) {
                result[key] = val.map((el) => { return el.id });
            }
            else if (valIsNumberArray) {
                result[key] = val;
            }
            else if (valIsObject && rowInList) {
                result[key] = val.id;
            }
            else if (valIsObject && !rowInList) {
                result[key] = val.id;
            }
            else {
                result[key] = val;
            }
        }
        return result;
    }



    /***** Url utils *****/

    static createUrlQuery(params) {
        if (!params) {
            return "";
        }

        let page;
        let perPage;
        let field;
        let order;
        let query = {};
        if (params.pagination) {
             page = params.pagination.page;
             perPage =  params.pagination.perPage;
             query.range = JSON.stringify([
                page,
                perPage,
            ]);
        }
        if (params.sort) {
            field = params.sort.field;
            order = params.sort.order;
            query.sort = JSON.stringify([field, order]);
        }
        if (!params.filter) {
            params.filter = {};
        }
        if (Array.isArray(params.ids)) {
            params.filter.id = params.ids;
        }
        if (params.filter) {
            query.filter = JSON.stringify(params.filter)
        }
        return `${stringify(query)}`;
    }


    /***** options utils *****/
    static createOptionsForImageUpload(post_data) {
        let options = {};
        let csrf_token = localStorage.getItem('csrf_token');
        options.method = 'POST';
        options.body = post_data;
        options.headers = new Headers({
            'X-Authorization': 'Bearer '+csrf_token,
           'Content-Transfer-Encoding': 'base64',
        });

        options.credentials = "include";
        return options;
    }



    static createOptionsForGET() {
        let options = {};
        let csrf_token = localStorage.getItem('csrf_token');
        options.method = 'GET';
        options.headers = new Headers({
            'Content-Type': 'application/json',
            'X-Authorization': 'Bearer '+csrf_token
        });

        // options.credentials = "include";
        return options;
    }

    static createOptionsForPOST(post_data) {
        let options = {};
        let csrf_token = localStorage.getItem('csrf_token');
        options.method = 'POST';
        options.body = post_data;
        options.headers = new Headers({
            'Content-Type': 'application/json',
            'X-Authorization': 'Bearer '+csrf_token
        });

        options.credentials = "include";
        return options;
    }

    static createOptionsForPUT(post_data) {
        let options = {};
        let csrf_token = localStorage.getItem('csrf_token');
        options.method = 'PUT';
        options.body = post_data;
        options.headers = new Headers({
            'Content-Type': 'application/json',
            'X-Authorization': 'Bearer '+csrf_token
        });

        options.credentials = "include";
        return options;
    }

    /**** filter utils ****/
    static prepAuthorizeFilter(filters) {
        if (!filters) {
            return {};
        }
        let userId = parseInt(localStorage.getItem('user_id'));
        if (APIUtils.isStaff()) {
            filters.staffId = [userId];
            filters.staff = [userId];
        }
        else if (APIUtils.isClient()) {
            filters.clientId = [userId];
            filters.client = [userId];
        }
        return filters;
    }

    static isAdmin() {
        let userRole = localStorage.getItem('user_role');
        return (userRole === 'admin');
    }

    static isClient() {
        let userRole = localStorage.getItem('user_role');
        return (userRole === 'client');
    }

    static isStaff() {
        let userRole = localStorage.getItem('user_role');
        return (userRole === 'staff');
    }
}