<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>{{ title }}</h2>
            </div>
        </div>
        <form>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary form-group">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">User Info</h4>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Email:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ user.userEmail }}</p>
                                    <input v-show="edit" name="userEmail" class="form-control" type="email" required v-model="user.userEmail" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">First Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ user.firstName }}</p>
                                    <input v-show="edit" name="firstName" class="form-control" type="text" v-model="user.firstName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Last Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ user.lastName }}</p>
                                    <input v-show="edit" name="lastName" class="form-control" type="text" v-model="user.lastName" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary form-group">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Password</h4>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Password:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">************</p>
                                    <input v-show="edit" name="password" class="form-control" type="password" required v-model="user.password" /> <!-- TODO make sure this doesn't risk compromising the password somehow... -->
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Repeat Password:</p>
                                </div>
                                <div class="col-sm-6">
                                    <input v-show="edit" name="repeatPassword" class="form-control" type="password" required v-model="user.repeatPassword" /> <!-- TODO make sure this doesn't cause problems -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary form-group">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Access Roles</h4>
                        </div>
                        <div id="accessCheckboxes" class="panel-body">
                            <div v-for="role in roles">
                                <input v-show="showCheckbox(role)" :id="role" type="checkbox" name="role" v-model="user.roles" :value="role" :disabled="disableCheckbox(role)" />
                                <label v-show="showCheckbox(role)" :for="role">{{ role }}</label>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary form-group">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Org</h4>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Org:</p>
                                </div>
                                <div class="col-sm-6">
                                    <!-- TODO this is where the org selector goes -->
                                    <p v-show="!edit">{{ user.orgName }}</p>
                                    <select v-show="edit" name="orgName" class="form-control" v-model="user.orgId">
                                        <option v-for="org in orgList" :value="org.orgId">{{ org.orgName }}</option>
                                    </select>
                                    <!--TODO need some way to preserve the orgId too -->
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <a class="btn btn-primary" type="button" title="Cancel changes">Cancel</a>
                    <button v-show="edit" class="btn btn-success" type="submit" title="Save changes">Save</button>
                    <a v-show="canEdit" class="btn btn-danger pull-right" type="button" title="Delete User">Delete</a>
                    <a v-show="canEdit && !edit" class="btn btn-info pull-right" type="button" title="Edit User" @click="startEdit">Edit</a>
                </div>
            </div>
        </form>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'user_content',
        data(){
            return {
                title: 'New User',
                user: {
                    userEmail: '',
                    password: '',
                    roles: [],
                    orgId: 0,
                    orgName: null,
                    firstName: '',
                    lastName: '',
                    userId: 0
                },
                orgList: [],
                edit: false,
                roles: orgbuilder.jwt.roles
            }
        },
        computed: {
            canEdit(){
                if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master) || orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin)){
                    return true;
                }
                else{
                    return this.$route.query.userId == orgbuilder.jwt.getTokenPayload().uid;
                }
            }
        },
        beforeMount(){
            orgbuilder.access.start(this)
                .hasAnyRole(orgbuilder.jwt.roles.master, orgbuilder.jwt.roles.admin)
                .next()
                .isUser(this.$route.query.userId)
                .validate();
        },
        mounted(){
            if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                this.loadOrgListAndUser();
            }
            else{
                this.loadUser();
            }

            //TODO if not master role, then select box should never appear
        },
        methods: {
            loadUser(){
                if(this.$route.query.userId !== undefined){
                    var app = this;
                    orgbuilder.api.get('users/' + this.$route.query.userId)
                        .done((user, status, jqXHR) => {
                            if(jqXHR.status === 204){
                                console.log('User not found on server');
                                app.$emit('showAlert', {
                                    show: true,
                                    msg: 'User not found on server',
                                    clazz: 'alert-danger'
                                });
                            }

                            app.user = user;
                            this.title = user.userEmail;
                        })
                        .fail((jqXHR) => console.log('FAILED TO RETRIEVE USER DETAILS: ' + jqXHR.status));
                }
            },
            loadOrgListAndUser(){
                var app = this;
                orgbuilder.api.get('orgs')
                    .done((data, status, jqXHR) => {
                        if(jqXHR.status === 204){
                            console.log('No orgs found on server');
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'No orgs found on server',
                                clazz: 'alert-danger'
                            });
                        }

                        app.orgList = data.orgList;
                        this.loadUser();
                    })
                    .fail((jqXHR) => console.log('FAILED TO RETRIEVE ORG LIST: ' + jqXHR.status));
            },
            startEdit(){
                this.edit = true;
            },
            disableCheckbox(role){
                if(!this.edit){
                    return true;
                }

                if('MASTER' === role){
                    if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                        return this.user.roles.indexOf('MASTER') < 0 && this.user.roles.length > 0;
                    }
                }
                else{
                    return this.user.roles.indexOf('MASTER') >= 0;
                }
            },
            showCheckbox(role){
                if('MASTER' === role){
                    return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master);
                }

                return true;
            }
        }
    }
</script>