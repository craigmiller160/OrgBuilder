import { orgbuilder } from './orgbuilder.js';

orgbuilder.jwt = (function(){
    const TOKEN_STORAGE_KEY = "orgapiToken";
    const BEARER_PREFIX = "Bearer";

    const roles = {
        master: 'MASTER',
        admin: 'ADMIN',
        write: 'WRITE',
        read: 'READ'
    };

    function stripBearerPrefix (token){
        if(typeof token === "string" && token.startsWith(BEARER_PREFIX)){
            return token.substring(6).trim();
        }
        return token;
    }

    function restoreBearerPrefix (token){
        if(typeof token === "string" && !token.startsWith(BEARER_PREFIX)){
            return BEARER_PREFIX + " " + token;
        }
        return token;
    }

    function storeToken(token){
        token = stripBearerPrefix(token);
        localStorage.setItem(TOKEN_STORAGE_KEY, token);
    }

    function getToken(){
        return localStorage.getItem(TOKEN_STORAGE_KEY);
    }

    function getTokenPayload(){
        var token = getToken();
        if(token === null || token === undefined || token === ''){
            console.log('Error! No token to parse payload from');
            return;
        }

        try{
            return JSON.parse(atob(token.split('.')[1]));
        }
        catch(e){
            console.log('Error! Unable to parse JSON payload in token');
            throw e;
        }
    }

    function clearToken(){
        delete localStorage[TOKEN_STORAGE_KEY];
    }

    function tokenExists(){
        return localStorage.getItem(TOKEN_STORAGE_KEY) !== undefined && localStorage.getItem(TOKEN_STORAGE_KEY) !== null;
    }

    function hasRole(role){
        try{
            return $.inArray(role, getTokenPayload().rol) !== -1;
        }
        catch(e){
            console.log('Error! Unable to check the role in the token payload');
            throw e;
        }
    }

    return {
        tokenKey: TOKEN_STORAGE_KEY,
        storeToken: storeToken,
        getToken: getToken,
        getTokenPayload: getTokenPayload,
        clearToken: clearToken,
        tokenExists: tokenExists,
        hasRole: hasRole,
        roles: roles,
        restoreBearerPrefix: restoreBearerPrefix
    }
})();