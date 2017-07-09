import { orgbuilder } from './orgbuilder.js'

orgbuilder.api = (() => {

    function get(uri){
        return send(uri, 'GET');
    }

    function post(uri, json){
        return send(uri, 'POST', json);
    }

    function put(uri, json){
        return send(uri, 'PUT', json);
    }

    function del (uri, json){
        return send(uri, 'DELETE', json);
    }

    function send(uri, method, json){
        console.log('Calling API: ' + method + ' ' + uri);
        return $.ajax({
            url: '/orgapi' + ensurePrecedingSlash(uri), //TODO this is where the origin prop should go
            type: method,
            headers: (() => {
                var result = {};

                var token = orgbuilder.jwt.getToken();
                if(token !== undefined && token !== null){
                    result.Authorization = orgbuilder.jwt.restoreBearerPrefix(token);
                }

                return result;
            })(),
            contentType: "application/json; charset=utf-8",
            data: (() => {
                if(json !== undefined){
                    return JSON.stringify(json);
                }
                return null;
            })()
        })
            .done((data, status, jqXHR) => {
                var token = jqXHR.getResponseHeader('Authorization');
                orgbuilder.jwt.storeToken(token);
            })
            .fail((jqXHR) => {
                var status = jqXHR.status;
                console.log('API Request Failed. Status: ' + status);
                if(status === 403){
                    //TODO go back to index.html with denied message
                    console.log('Bad request');
                    window.location.href = '/#/?bad=true';
                }
                else if(status === 0){
                    console.log('SERVER ERROR!');
                    //TODO show an alert in the UI
                }
                else if(status === 401){
                    //This comes up during a bad login or if the token has expired
                    //TODO return to the login page
                    console.log('Error Message: ' + jqXHR.responseText);
                }
                else if(status >= 500){
                    var error = jqXHR.responseJSON;
                    console.log('Critical server error, please check server logs for details');
                    console.log('Error Message: ' + error.exceptionName + ': ' + error.errorMessage);
                    //TODO show alert in UI
                }
                else{
                    console.log('Error communicating with server. Status: ' + status + ' ' + jqXHR.statusText);
                    console.log('Error Message: ' + jqXHR.responseJSON.errorMessage);
                    //TODO show alert in UI
                }
            });
    }

    function ensurePrecedingSlash(uri){
        if(uri.startsWith("/")){
            return uri;
        }
        return "/" + uri;
    }

    return {
        get: get,
        post: post,
        put: put,
        del: del
    }

})();