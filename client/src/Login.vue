<template>
    <div class="row">
        <div class="col-xs-12 col-sm-6 col-sm-offset-3">
            <form v-on:submit.prevent="logIn">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h4 class="text-center">Welcome</h4>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input v-model="credentials.userEmail" id="email" type="email" class="form-control" title="Email address" placeholder="Email address" required autofocus />
                            <label for="password">Password</label>
                            <input v-model="credentials.password" id="password" type="password" class="form-control" title="Password" placeholder="Password" required />
                        </div>
                        <button id="login" class="btn btn-primary btn-block" type="submit">Login</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js'
    export default {
        name: 'login',
        data() {
            return {
                credentials: {
                    userEmail: '',
                    password: ''
                }
            }
        },
        methods: {
            logIn(event){
                var app = this;

                orgbuilder.api.post('auth', this.credentials)
                    .done(() => {
                        app.$emit('loggedIn', true);
                        window.location.href = '/';
                    })
                    .fail(() => {
                        app.credentials.password = '';
                        app.$emit('loggedIn', false);
                        orgbuilder.vue.alert.showError(app, 'Login failed');
                    });
            }
        }
    }
</script>

<style>
    .form-group {
        padding-top: 20px;
        padding-bottom: 20px;
    }

    form {
        margin-top: 8em;
    }
</style>