<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>{{ title }}</h2>
            </div>
        </div>
        <form v-on:submit.prevent="saveChanges">
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
                                    <button v-show="edit" type="button" class="btn btn-info" @click="showChangePassword">Change Password</button>
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
                                    <p v-show="!edit">{{ user.orgName }}</p>
                                    <select v-show="showOrgSelectBox && edit" name="orgName" class="form-control" v-model="user.orgId">
                                        <option v-for="org in orgList" :value="org.orgId">{{ org.orgName }}</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <a class="btn btn-primary" type="button" title="Cancel changes" @click="handleCancel">Cancel</a>
                    <button v-show="edit" class="btn btn-success" type="submit" title="Save changes">Save</button>
                    <a v-show="canEdit && showDeleteBtn" class="btn btn-danger pull-right" type="button" title="Delete User" @click="showDeleteModal">Delete</a>
                    <a v-show="canEdit && !edit" class="btn btn-info pull-right" type="button" title="Edit User" @click="startEdit">Edit</a>
                </div>
            </div>
        </form>
        <app-modal :context="modalContext"
                   v-on:result="modalResult($event)">
        </app-modal>
        <div id="passwordModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" @click="handlePassword(false)">&times;</button>
                        <h2 class="modal-title">Change Password</h2>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-12">
                                <div id="passwordModalAlert" class="alert alert-danger hidden">
                                    <a :href="passwordModalAlertClose" class="close" data-hide="alert" aria-label="close">&times;</a>
                                    <p>Password fields do not match, cannot save changes.</p>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Password:</p>
                            </div>
                            <div class="col-sm-6">
                                <input name="password" class="form-control" type="password" required v-model="user.password" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Repeat Password:</p>
                            </div>
                            <div class="col-sm-6">
                                <input name="repeatPassword" class="form-control" type="password" required v-model="user.repeatPassword" />
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="modal-yes btn btn-success" @click="handlePassword(true)">Save</button>
                        <button type="button" class="modal-no btn btn-danger" @click="handlePassword(false)">Cancel</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import ConfirmModal from './ConfirmModal.vue';
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
                roles: orgbuilder.jwt.roles,
                modalContext: {
                    id: 0,
                    type: ''
                }
            }
        },
        components: {
            'app-modal': ConfirmModal
        },
        computed: {
            canEdit(){
                if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master) || orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin)){
                    return true;
                }
                else{
                    return this.$route.query.userId == orgbuilder.jwt.getTokenPayload().uid;
                }
            },
            showDeleteBtn(){
                return this.$route.query.userId !== undefined;
            },
            showOrgSelectBox(){
                return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master) && this.user.roles.indexOf(orgbuilder.jwt.roles.master) === -1;
            },
            passwordModalAlertClose(){
                return '/#' + this.$route.fullPath;
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
                this.orgList = [
                    {
                        orgId: orgbuilder.jwt.getTokenPayload().oid,
                        orgName: orgbuilder.jwt.getTokenPayload().onm
                    }
                ];
                this.loadUser();
            }

            if(this.$route.query.userId === undefined){
                this.edit = true;
            }
        },
        methods: {
            showChangePassword(){
                $('#passwordModalAlert').addClass('hidden');
                $('#passwordModal').modal({
                    backdrop: 'static'
                });
            },
            handlePassword(isSave){
                if(!isSave){
                    //If closing without saving, delete the property values
                    delete this.user.password;
                    delete this.user.repeatPassword;
                    $('#passwordModal').modal('hide');
                }
                else{
                    if(orgbuilder.varExistsString(this.user.password) && orgbuilder.varExistsString(this.user.repeatPassword) &&
                        this.user.password === this.user.repeatPassword){
                        //If saving and the password and repeat password are the same, delete the repeat password, everything else is good
                        delete this.user.repeatPassword;
                        $('#passwordModal').modal('hide');
                    }
                    else{
                        //If they're not the same, delete both values and show the error alert
                        delete this.user.password;
                        delete this.user.repeatPassword;
                        $('#passwordModalAlert').removeClass('hidden');
                    }
                }
            },
            loadUser(){
                if(this.$route.query.userId !== undefined){
                    const app = this;
                    orgbuilder.api.get('users/' + this.$route.query.userId)
                        .done((user, status, jqXHR) => {
                            if(jqXHR.status === 204){
                                console.log('User not found on server');
                                app.$emit('showAlert', {
                                    show: true,
                                    msg: 'User not found on server',
                                    clazz: 'alert-danger'
                                });
                                return;
                            }

                            app.user = user;
                            this.title = user.userEmail;
                        })
                        .fail((jqXHR) => console.log('FAILED TO RETRIEVE USER DETAILS: ' + jqXHR.status));
                }
                else{
                    this.user.orgId = orgbuilder.jwt.getTokenPayload().oid;
                    this.user.orgName = orgbuilder.jwt.getTokenPayload().onm;
                }
            },
            loadOrgListAndUser(){
                const app = this;
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
            },
            handleCancel(){
                if(this.edit){
                    this.modalContext.type = 'Cancel';
                    $('#confirmModal').modal({
                        backdrop: 'static'
                    });
                }
                else if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin) || orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                    window.location.href = '/#/users/manage';
                }
                else{
                    window.location.href = '/#/';
                }
            },
            showDeleteModal(){
                this.modalContext.id = this.$route.query.userId;
                if(this.$route.query.userId == orgbuilder.jwt.getTokenPayload().uid){
                    this.modalContext.type = 'Delete Your Account';
                }
                else{
                    this.modalContext.type = 'Delete';
                }

                $('#confirmModal').modal({
                    backdrop: 'static'
                });
            },
            modalResult(arg){
                const app = this;
                if((arg.context.type === 'Delete' || arg.context.type === 'Delete Your Account') && arg.status){
                    orgbuilder.api.del('users/' + arg.context.id)
                        .done((data) => {
                            console.log('User successfully deleted');
                            if(arg.context.type === 'Delete'){
                                window.location.href = '/#/users/manage';
                                app.$emit('showAlert', {
                                    show: true,
                                    msg: 'User successfully deleted',
                                    clazz: 'alert-success'
                                });
                            }
                            else{
                                orgbuilder.jwt.clearToken();
                                window.location.href = '/#/login';
                            }
                        })
                        .fail(() => console.log('User delete FAILED'));
                }
                else if(arg.context.type === 'Cancel' && arg.status){
                    if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin) || orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                        window.location.href = '/#/users/manage';
                    }
                    else{
                        window.location.href = '/#/';
                    }
                }
            },
            saveChanges(){
                const app = this;

                const doneFn = function(user) {
                    app.edit = false;
                    app.title = user.userEmail;
                    app.user = user;
                    if(app.$route.query.userId === undefined){
                        window.location.href = window.location.href + '?userId=' + user.userId;
                    }
                    app.$emit('showAlert', {
                        show: true,
                        msg: 'User saved',
                        clazz: 'alert-success'
                    });
                };

                const failFn = function(){
                    app.$emit('showAlert', {
                        show: true,
                        msg: 'Save failed.',
                        clazz: 'alert-danger'
                    });
                };

                if(this.$route.query.userId !== undefined){
                    orgbuilder.api.put('users/' + this.$route.query.userId, this.user)
                        .done(doneFn)
                        .fail(failFn);
                }
                else{
                    orgbuilder.api.post('users', this.user)
                        .done(doneFn)
                        .fail(failFn);
                }
            }
        }
    }
</script>