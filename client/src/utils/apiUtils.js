import 'isomorphic-fetch';

export const BASE_URL = 'http://localhost:10000/api/v1';
export const TOKEN = 'token';

/**
 * Make an HTTP request.
 *
 * @param {string} url where to make the request.
 * @param {object} [config] request configuration.
 * @param {object} [request] request object.
 * @param {function} [onSuccess] success event function.
 * @param {function} [onFailure] failure event function.
 * @returns {function()}
 */
export function request(url, config = {}, request, onSuccess, onFailure) {
    return dispatch => {
        if (request) {
            dispatch(request);
        }

        const headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json, text/plain');

        // If authentication token exists, use it in request.
        const token = getToken();
        if (token) {
            headers.append('Authorization', 'Bearer ' + token);
        }

        return fetch(BASE_URL + url, {
            method: config.method,
            body: config.body,
            mode: 'cors',
            headers: headers
        }).then((response) => {
            // Check if status is ok.
            if (!response.ok) {
                const error = new Error(response.statusText);
                error.response = response;
                throw error;
            }
            return response;
        }).then((response) => {
            return response.text().then(function (text) {
                return text ? JSON.parse(text) : {}
            });
        }).then((json) => {
            // Dispatch success event with json param.
            if (typeof onSuccess === 'function') {
                dispatch(onSuccess(json));
            }
        }).catch((error) => {
            // Format error and dispatch error events.
            const response = error.response;
            if (response === undefined) {
                if (typeof onFailure === 'function') {
                    dispatch(onFailure(error));
                }
            } else {
                error.status = response.status;
                error.statusText = response.statusText;
                response.text().then((text) => {
                    try {
                        const json = JSON.parse(text);
                        error.message = json.error;
                    } catch (ex) {
                        error.message = text;
                    }
                    if (typeof onFailure === 'function') {
                        dispatch(onFailure(error));
                    }
                });
            }
        });
    };
}

/**
 * Set authentication token value.
 *
 * @param {string} token new token value.
 */
export function setToken(token) {
    localStorage.setItem(TOKEN, token);
}

/**
 * Remove authentication token from storage.
 */
export function removeToken() {
    localStorage.removeItem(TOKEN);
}

/**
 * Get authentication token from storage.
 *
 * @returns {string} token value.
 */
export function getToken() {
    return localStorage.getItem(TOKEN);
}