import { AUTH_LOGIN, AUTH_LOGOUT, AUTH_ERROR, AUTH_CHECK, AUTH_GET_PERMISSIONS } from 'admin-on-rest';
import { fetchUtils } from 'admin-on-rest';
import { config } from './config';
import APIUtils from './apiUtils';

class UserHandler {

    static throwOnError = (response) => {
        if (!response.ok) {
            return response.json().then((res) => {
                return Promise.reject(res.message);
            });
        }
        return response;
    };

    static logoutCallback() {
        localStorage.removeItem('csrf_token');
        localStorage.removeItem('username');
        localStorage.removeItem('user_role');
        localStorage.removeItem('user_id');
    }

    static isLoggedIn() {
        return localStorage.getItem('username') &&
            localStorage.getItem('csrf_token')
    }

    static login(username, password) {
        const options = {
            method: 'POST',
            body: JSON.stringify({ username, password }),
            headers: new Headers({ 'Content-Type': 'application/json' }),
        };
        return fetchUtils.fetchJson(config.loginEndpoint, options);
    }

    static getUserDetails() {
        const request = new Request(config.profileEndpoint, APIUtils.createOptionsForGET());

        return fetch(request).then(UserHandler.throwOnError).then((resp) => {
            return resp.json().then((result) => {
                if (result.content && result.content[0].role && result.content[0].id) {
                    let userId = result.content[0].id;
                    let userRole = result.content[0].role.toLowerCase();
                    if (userRole.indexOf('client') !== -1){
                        userRole = 'client';
                    }
                    else if(userRole.indexOf('admin') !== -1){
                        userRole = 'admin';
                    }
                    else if(userRole.indexOf('staff') !== -1){
                        userRole = 'staff';
                    }
                    else {
                        userRole = 'unknown_role';
                    }

                    return {userId: userId, userRole: userRole};
                }
            })
        });
    }

}

export default (type, params) => {
    // called when the user attempts to log in
    if (type === AUTH_LOGIN) {
        const { username, password } = params;
        localStorage.setItem('username', username);
        let requestLogin = UserHandler.login(username, password);
        return requestLogin.then(response => {
            let json = JSON.parse(response.body);
            localStorage.setItem('csrf_token', json.token);
            let userDetailsPromise = UserHandler.getUserDetails();
            return Promise.all([userDetailsPromise]).then(values => {
                let userRole = values[0].userRole;
                let userId = values[0].userId;
                localStorage.setItem('user_role', userRole);
                localStorage.setItem('user_id', userId);
            });
        });
    }
    // called when the user clicks on the logout button
    else if (type === AUTH_LOGOUT) {
        UserHandler.logoutCallback();
        return Promise.resolve();
    }
    // called when the API returns an error
    else if (type === AUTH_ERROR) {
        const { status } = params;
        if (status === 401 || status === 403) {
            UserHandler.logoutCallback();
            return Promise.reject();
        }
        return Promise.resolve();
    }
    // called when the user navigates to a new location
    else if (type === AUTH_CHECK) {
        const { resource } = params;
        // TODO: possibly can be used with roles per resource
        if (resource === 'dashboard') {
        }
        return UserHandler.isLoggedIn() ? Promise.resolve() : Promise.reject();
    }
    else if (type === AUTH_GET_PERMISSIONS) {
        return Promise.resolve(localStorage.getItem('user_role'));
    }
    return Promise.reject('Unknown method');
};
