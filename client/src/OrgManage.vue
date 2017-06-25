<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>Orgs</h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <div class="panel panel-primary">
                    <table class="table table-primary table-striped">
                        <thead>
                            <tr>
                                <th>Org Name</th>
                                <th>Created Date</th>
                                <th>Options</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="org in orgList" :orgId="org.orgId">
                                <td>{{ org.orgName }}</td>
                                <td>{{ org.createdDate }}</td>
                                <td>
                                    <a :href="'/#/orgs/content?orgId=' + org.orgId" class="btn btn-info" title="Edit Org">Edit</a>
                                    <a class="btn btn-danger" title="Delete Org" @click="showModal">Delete</a>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <a class="btn btn-primary" href="/#/orgs/content" title="Add new Org">Add</a>
            </div>
        </div>
        <app-modal :context="modalContext"
                   v-on:result="modalResult($event)">
        </app-modal>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';
    import ConfirmModal from './ConfirmModal.vue';

    export default {
        name: 'org_manage',
        data(){
            return {
                orgList: [],
                modalContext: {
                    type: '',
                    id: 0
                }
            }
        },
        components: {
            'app-modal': ConfirmModal
        },
        beforeMount(){
            orgbuilder.access.hasMasterAccess(this);
        },
        mounted(){
            this.loadOrgs();
        },
        methods: {
            loadOrgs(){
                orgbuilder.api.get('orgs')
                    .done((data,status,jqXHR) => {
                        if(jqXHR.status === 204){
                            console.log("No orgs found on server");
                            return;
                        }

                        this.orgList = data.orgList;
                    });
            },
            showModal(event){
                this.modalContext.id = $(event.target).parents('tr').attr('orgId');
                this.modalContext.type = 'Delete';
                $(".modal").modal({
                    backdrop: 'static'
                });
            },
            modalResult(arg){
                if(arg.status){
                    var app = this;

                    orgbuilder.api.del('orgs/' + arg.context.id)
                        .done(() => {
                            console.log('Org successfully deleted');
                            app.loadOrgs();
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'Org successfully deleted',
                                clazz: 'alert-success'
                            })
                        })
                        .fail(() => console.log("Org delete FAILED"));
                }
            }
        }
    }
</script>

<style>



</style>