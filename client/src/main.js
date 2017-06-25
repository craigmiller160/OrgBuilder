//Import these orgbuilder js files so the orgbuilder object can be built properly and provided as a single import
import './js/orgbuilder.js';
import './js/org-jwt.js';
import './js/org-api.js';
import './js/org-access';

import Vue from 'vue';
import VueRouter from 'vue-router';
import App from './App.vue';
import Login from './Login.vue';

Vue.use(VueRouter);

const routes = [
    {
        path: '/login',
        component: Login
    }
];

const router = new VueRouter({
    routes: routes
});

new Vue({
    el: '#app',
    router: router,
    render: h => h(App)
});
