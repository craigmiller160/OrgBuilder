<template>
    <div>
        <div class="row">
            <div class="col-xs-12">
                <h2 class="text-center">{{ title }}</h2>
            </div>
        </div>
        <form v-on:submit.prevent="saveChanges">
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary form-group">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Org Info</h4>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Org Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ org.orgName }}</p>
                                    <input v-show="edit" name="orgName" class="form-control" type="text" required v-model="org.orgName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Created Date:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p>{{ org.createdDate }}</p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Org Description:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ org.orgDescription }}</p>
                                    <textarea v-show="edit" name="orgDescription" class="form-control" maxLength="255" rows="5" v-model="org.orgDescription"></textarea>
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
                    <a v-show="canEdit && showDelete" class="btn btn-danger pull-right" type="button" title="Delete Org" @click="showDeleteModal">Delete</a>
                    <a v-show="canEdit && !edit" class="btn btn-info pull-right" type="button" title="Edit Org" @click="startEdit">Edit</a>
                </div>
            </div>
        </form>
        <app-modal :context="modalContext"
                   v-on:result="modalResult($event)">
        </app-modal>
    </div>
</template>

<script>
    import ConfirmModal from './ConfirmModal.vue';
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'org_content',
        data(){
            return {
                title: 'New Org',
                edit: false,
                org: { //createdDate is being left out so not to mess with adding a new org
                    orgId: 0,
                    orgName: '',
                    orgDescription: ''
                },
                modalContext: {
                    id: 0,
                    type: ''
                }
            }
        },
        components: {
            'app-modal': ConfirmModal
        },
        beforeMount(){
            orgbuilder.access
                .start(this)
                .hasAnyRole(orgbuilder.jwt.roles.master)
                .next()
                .isOrg(this.$route.query.orgId)
                .validate();
        },
        mounted(){
            if(this.$route.query.orgId !== undefined){
                const app = this;

                orgbuilder.api.get('orgs/' + this.$route.query.orgId)
                    .done((org, status, jqXHR) => {
                        if(jqXHR.status === 204){
                            console.log("Org not found on server");
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'Org not found on server',
                                clazz: 'alert-danger'
                            });
                            return;
                        }

                        app.org = org;
                        app.title = org.orgName;
                    })
                    .fail((jqXHR) => console.log('FAILED TO RETRIEVE ORG DETAILS: ' + jqXHR.status));
            }
            else{
                this.edit = true;
            }
        },
        methods: {
            startEdit(){
                this.edit = true;
            },
            saveChanges(){
                const app = this;

                const doneFn = function(org){
                    app.edit = false;
                    app.title = org.orgName;
                    app.org = org;
                    if(app.$route.query.orgId === undefined){
                        window.location.href = window.location.href + '?orgId=' + org.orgId;
                    }
                    app.$emit('showAlert', {
                        show: true,
                        msg: 'Org saved',
                        clazz: 'alert-success'
                    });
                };

                const failFn = function(message){
                    app.$emit('showAlert', {
                        show: true,
                        msg: 'Save failed. Message: ' + message,
                        clazz: 'alert-danger'
                    });
                };

                if(this.$route.query.orgId !== undefined){
                    orgbuilder.api.put('orgs/' + this.$route.query.orgId, this.org)
                        .done(doneFn)
                        .fail(failFn);
                }
                else{
                    orgbuilder.api.post('orgs', this.org)
                        .done(doneFn)
                        .fail(failFn);
                }
            },
            showDeleteModal(){
                this.modalContext.id = this.$route.query.orgId;
                this.modalContext.type = 'Delete';
                $(".modal").modal({
                    backdrop: 'static'
                });
            },
            handleCancel(){
                if(this.edit){
                    this.modalContext.type = 'Cancel';
                    $('.modal').modal({
                        backdrop: 'static'
                    });
                }
                else if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                    window.location.href = '/#/orgs/manage';
                }
                else{
                    window.location.href = '/#/';
                }
            },
            modalResult(arg){
                const app = this;
                if(arg.context.type === 'Delete' && arg.status){
                    orgbuilder.api.del('orgs/' + arg.context.id)
                        .done((data) => {
                            console.log('Org successfully deleted');
                            window.location.href = '/#/orgs/manage';
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'Org successfully deleted',
                                clazz: 'alert-success'
                            })
                        })
                        .fail(() => console.log("Org delete FAILED"));
                }
                else if(arg.context.type === 'Cancel' && arg.status){
                    if(orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master)){
                        window.location.href = '/#/orgs/manage';
                    }
                    else{
                        window.location.href = '/#/';
                    }
                }
            }
        },
        computed: {
            canEdit(){
                //The admin role needing the same orgId gets checked for during the access check
                return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.master) || orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.admin);
            },
            showDelete(){
                return this.$route.query.orgId !== undefined;
            }
        }
    }
</script>