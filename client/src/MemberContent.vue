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
                            <h4 class="pull-left">Personal Info</h4>
                        </div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">First Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.firstName }}</p>
                                    <input v-show="edit" name="firstName" class="form-control" type="text" v-model="member.firstName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Middle Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.middleName }}</p>
                                    <input v-show="edit" name="middleName" class="form-control" type="text" v-model="member.middleName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Last Name:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.lastName }}</p>
                                    <input v-show="edit" name="lastName" class="form-control" type="text" v-model="member.lastName" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Date of Birth:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.dateOfBirth }}</p>
                                    <input v-show="edit" name="dateOfBirth" class="form-control" type="date" v-model="member.dateOfBirth" />
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <p class="content-name">Sex:</p>
                                </div>
                                <div class="col-sm-6">
                                    <p v-show="!edit">{{ member.sex }}</p>
                                    <select v-show="edit" name="sex" class="form-control" v-model="member.sex">
                                        <option v-for="sex in info.sexes" :value="sex">{{ sex }}</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Addresses</h4>
                        </div>
                        <div class="panel-body">
                            <div v-for="(address, index) in member.addresses" class="row" :addressId="address.addressId">
                                <div class="col-sm-1">
                                    <p>{{ address.preferred ? '*' : '' }}</p>
                                </div>
                                <div class="col-sm-2">
                                    <p>{{ address.addressType }}</p>
                                </div>
                                <div class="col-sm-5">
                                    <p>{{ parseAddress(address) }}</p>
                                </div>
                                <div class="col-sm-4">
                                    <button v-show="edit" type="button" class="btn btn-info" title="Edit Address" @click="(event) => editContactInfo('address', index)">Edit</button>
                                    <button v-show="edit" type="button" class="btn btn-danger" title="Delete Address">Delete</button>
                                </div>
                            </div>
                            <div v-show="edit" class="row">
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-primary" title="Add Address" @click="(event) => editContactInfo('address')">Add</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Phones</h4>
                        </div>
                        <div class="panel-body">
                            <div v-for="(phone, index) in member.phones" class="row" :phoneId="phone.phoneId">
                                <div class="col-sm-1">
                                    <p>{{ phone.preferred ? '*' : '' }}</p>
                                </div>
                                <div class="col-sm-2">
                                    <p>{{ phone.phoneType }}</p>
                                </div>
                                <div class="col-sm-5">
                                    <p>{{ parsePhone(phone) }}</p>
                                </div>
                                <div class="col-sm-4">
                                    <button v-show="edit" type="button" class="btn btn-info" title="Edit Phone" @click="(event) => editContactInfo('phone', index)">Edit</button>
                                    <button v-show="edit" type="button" class="btn btn-danger" title="Delete Phone">Delete</button>
                                </div>
                            </div>
                            <div v-show="edit" class="row">
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-primary" title="Add Phone" @click="(event) => editContactInfo('phone')">Add</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-primary">
                        <div class="panel-heading clearfix">
                            <h4 class="pull-left">Emails</h4>
                        </div>
                        <div class="panel-body">
                            <div v-for="(email, index) in member.emails" class="row" :emailId="email.emailId">
                                <div class="col-sm-1">
                                    <p>{{ email.preferred ? '*' : '' }}</p>
                                </div>
                                <div class="col-sm-2">
                                    <p>{{ email.emailType }}</p>
                                </div>
                                <div class="col-sm-5">
                                    <p>{{ email.emailAddress }}</p>
                                </div>
                                <div class="col-sm-4">
                                    <button v-show="edit" type="button" class="btn btn-info" title="Edit Email" @click="(event) => editContactInfo('email', index)">Edit</button>
                                    <button v-show="edit" type="button" class="btn btn-danger" title="Delete Email">Delete</button>
                                </div>
                            </div>
                            <div v-show="edit" class="row">
                                <div class="col-sm-6">
                                    <button type="button" class="btn btn-primary" title="Add Email" @click="(event) => editContactInfo('email')">Add</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                    <a class="btn btn-primary" type="button" title="Cancel Changes" @click="handleCancel">Cancel</a>
                    <a v-show="edit" class="btn btn-success" type="submit" title="Save Changes" @click="saveChanges">Save</a>
                    <a v-show="canEdit && showDeleteBtn" class="btn btn-danger pull-right" type="button" title="Delete Member" @click="deleteMember">Delete</a>
                    <a v-show="canEdit && !edit" class="btn btn-info pull-right" type="button" title="Edit Member" @click="startEdit">Edit</a>
                </div>
            </div>
        </form>
        <app-modal :context="modalContext"
                   v-on:result="modalResult($event)">
        </app-modal>
        <div id="addressModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Address</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Address Type:</p>
                            </div>
                            <div class="col-sm-6">
                                <select v-if="member.addresses[selectedAddressIndex] !== undefined" name="addressType" class="form-control" v-model="member.addresses[selectedAddressIndex].addressType">
                                    <option v-for="type in info.contactTypes.addressTypes" :value="type">{{ type }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Address Line 1:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="address1" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].address1" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Address Line 2:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="address2" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].address2" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">City:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="city" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].city" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">State:</p>
                            </div>
                            <div class="col-sm-6">
                                <select v-if="member.addresses[selectedAddressIndex] !== undefined" name="state" class="form-control" v-model="member.addresses[selectedAddressIndex].state">
                                    <option v-for="state in info.states" :value="state">{{ state }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Zip Code:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" name="zipCode" class="form-control" type="text" v-model="member.addresses[selectedAddressIndex].zipCode" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-sm-offset-6">
                                <input v-if="member.addresses[selectedAddressIndex] !== undefined" id="preferredAddress" name="preferred" type="checkbox" v-model="member.addresses[selectedAddressIndex].preferred" @click="preferredChange('address')" />
                                <label for="preferredAddress">Preferred</label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="phoneModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Phone</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Phone Type:</p>
                            </div>
                            <div class="col-sm-6">
                                <select v-if="member.phones[selectedPhoneIndex] !== undefined" name="phoneType" class="form-control" v-model="member.phones[selectedPhoneIndex].phoneType">
                                    <option v-for="type in info.contactTypes.phoneTypes" :value="type">{{ type }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Area Code:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.phones[selectedPhoneIndex] !== undefined" name="areaCode" class="form-control" type="text" v-model="member.phones[selectedPhoneIndex].areaCode" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Prefix:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.phones[selectedPhoneIndex] !== undefined" name="prefix" class="form-control" type="text" v-model="member.phones[selectedPhoneIndex].prefix" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Line Number:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.phones[selectedPhoneIndex] !== undefined" name="lineNumber" class="form-control" type="text" v-model="member.phones[selectedPhoneIndex].lineNumber" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Extension:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.phones[selectedPhoneIndex] !== undefined" name="extension" class="form-control" type="text" v-model="member.phones[selectedPhoneIndex].extension" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-sm-offset-6">
                                <input id="preferredPhone" v-if="member.phones[selectedPhoneIndex] !== undefined" name="preferred" type="checkbox" v-model="member.phones[selectedPhoneIndex].preferred" @click="preferredChange('phone')" />
                                <label for="preferredPhone">Preferred</label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
        <div id="emailModal" class="modal" role="dialog">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Email</h4>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Email Type:</p>
                            </div>
                            <div class="col-sm-6">
                                <select v-if="member.emails[selectedEmailIndex] !== undefined" name="emailType" class="form-control" v-model="member.emails[selectedEmailIndex].emailType">
                                    <option v-for="type in info.contactTypes.emailTypes" :value="type">{{ type }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <p class="content-name">Email Address:</p>
                            </div>
                            <div class="col-sm-6">
                                <input v-if="member.emails[selectedEmailIndex] !== undefined" name="emailAddress" class="form-control" type="text" v-model="member.emails[selectedEmailIndex].emailAddress" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6 col-sm-offset-6">
                                <input id="preferredEmail" v-if="member.emails[selectedEmailIndex] !== undefined" name="preferred" type="checkbox" v-model="member.emails[selectedEmailIndex].preferred" @click="preferredChange('email')" />
                                <label for="preferredEmail">Preferred</label>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';
    import ConfirmModal from './ConfirmModal.vue';

    const emptyAddr = {
        addressId: null,
        addressType: 'HOME',
        address1: null,
        address2: null,
        city: null,
        state: null,
        zipCode: null,
        preferred: false,
        memberId: null
    };

    const emptyPhone = {
        phoneId: null,
        phoneType: 'HOME',
        areaCode: null,
        prefix: null,
        lineNumber: null,
        extension: null,
        preferred: false,
        memberId: null
    };

    const emptyEmail = {
        emailId: null,
        emailType: 'PERSONAL',
        emailAddress: null,
        preferred: false,
        memberId: null
    };

    export default {
        name: 'member_content',
        components: {
            'app-modal': ConfirmModal
        },
        data(){
            return {
                edit: false,
                member: {
                    memberId: 0,
                    dateOfBirth: null,
                    firstName: null,
                    middleName: null,
                    lastName: null,
                    sex: null,
                    phones: [],
                    emails: [],
                    addresses: []
                },
                selectedAddressIndex: 0,
                selectedPhoneIndex: 0,
                selectedEmailIndex: 0,
                info: {
                    appInfo: {},
                    sexes: [],
                    states: [],
                    roles: [],
                    contactTypes: {
                        addressTypes: [],
                        phoneTypes: [],
                        emailTypes: []
                    }
                },
                modalContext: {
                    type: '',
                    id: 0
                }
            }
        },
        beforeMount(){
            orgbuilder.access.start(this)
                .hasAnyRole(orgbuilder.jwt.roles.write, orgbuilder.jwt.roles.read)
                .validate();
        },
        mounted(){
            this.loadData();
            if(this.$route.query.memberId === undefined && this.canEdit){
                this.edit = true;
            }
        },
        computed: {
            canEdit(){
                return orgbuilder.jwt.hasRole(orgbuilder.jwt.roles.write);
            },
            showDeleteBtn(){
                return this.$route.query.memberId !== undefined;
            },
            title(){
                let title = '';
                if(orgbuilder.varExistsString(this.member.firstName)){
                    title = this.member.firstName;
                }

                if(orgbuilder.varExistsString(this.member.lastName)){
                    title = (title.length > 0 ? title + ' ' : '') + this.member.lastName;
                }

                if(orgbuilder.varExistsString(title)){
                    return title;
                }
                else{
                    return 'New Member';
                }
            }
        },
        methods: {
            startEdit(){
                if(this.canEdit){
                    this.edit = true;
                }
            },
            loadData(){
                if(this.info.sexes.length === 0){
                    this.loadInfoAndMember();
                }
                else{
                    this.loadMember();
                }
            },
            loadInfoAndMember(){
                const app = this;
                orgbuilder.api.get('info')
                    .done((data) => {
                        app.info = data;
                        app.loadMember();
                    });
            },
            loadMember(){
                const app = this;
                if(app.$route.query.memberId !== undefined){
                    orgbuilder.api.get('members/' + app.$route.query.memberId)
                        .done((data, status, jqXHR) => {
                            if(jqXHR.status === 204){
                                console.log('Member not found on server');
                                app.$emit('showAlert', {
                                    show: true,
                                    msg: 'Member not found on server',
                                    clazz: 'alert-danger'
                                });
                                return;
                            }

                            app.member = data;
                        })
                        .fail(() => console.log('Load member FAILED'));
                }
            },
            modalResult(arg){
                const app = this;
                if(arg.context.type === 'Cancel' && arg.status){
                    window.location.href = '/#/members/manage';
                }
                else if(arg.context.type === 'Delete' && arg.status){
                    orgbuilder.api.del('members/' + arg.context.id)
                        .done((data) => {
                            console.log('Member deleted successfully');
                            window.location.href = '/#/members/manage';
                            app.$emit('showAlert', {
                                show: true,
                                msg: 'Member successfully deleted',
                                clazz: 'alert-success'
                            });
                        })
                        .fail(() => console.log('Member delete FAILED'));
                }
            },
            saveChanges(event){
                const app = this;

                const doneFn = function(member){
                    app.edit = false;
                    app.member = member;
                    if(app.$route.query.memberId === undefined){
                        window.location.href = window.location.href + '?memberId=' + member.memberId;
                    }

                    app.$emit('showAlert', {
                        show: true,
                        msg: 'Member saved',
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

                if(this.$route.query.memberId !== undefined){
                    orgbuilder.api.put('members/' + this.$route.query.memberId, this.member)
                        .done(doneFn)
                        .fail(failFn);
                }
                else{
                    orgbuilder.api.post('members', this.member)
                        .done(doneFn)
                        .fail(failFn);
                }
            },
            handleCancel(event){
                if(this.edit){
                    this.modalContext.type = 'Cancel';
                    $('#confirmModal').modal({
                        backdrop: 'static'
                    });
                }
                else{
                    window.location.href = '/#/members/manage';
                }
            },
            deleteMember(event){
                this.modalContext.id = this.$route.query.memberId;
                this.modalContext.type = 'Delete';

                $('#confirmModal').modal({
                    backdrop: 'static'
                });
            },
            parseAddress(address){
                let text = '';
                text = orgbuilder.varExistsString(address.address1) ? address.address1 : '';
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.address2) ? address.address2 : '');
                text = text !== '' ? text + ',' : '';
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.city) ? address.city + ',' : '');
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.state) ? address.state : '');
                text = (text !== '' ? text + ' ' : '') + (orgbuilder.varExistsString(address.zipCode) ? address.zipCode : '');

                return text;
            },
            parsePhone(phone){
                let text = '';
                text = phone.areaCode !== null ? '(' + phone.areaCode + ')' : '';
                text = (text !== '' ? text + ' ' : '') + (phone.prefix !== null ? phone.prefix : '');
                text = text + '-';
                text = text + (phone.lineNumber !== null ? phone.lineNumber : '');
                text = text + (phone.extension !== null ? ' x' + phone.extension : '');

                return text;
            },
            editContactInfo(type, index){
                if('address' === type){
                    if(index === undefined){
                        this.selectedAddressIndex = this.member.addresses.length;
                        this.member.addresses.push($.extend({}, emptyAddr));
                    }
                    else{
                        this.selectedAddressIndex = index;
                    }

                    $('#addressModal').modal({
                        background: 'static'
                    });
                }
                else if('phone' === type){
                    if(index === undefined){
                        this.selectedPhoneIndex = this.member.phones.length;
                        this.member.phones.push($.extend({}, emptyPhone));
                    }
                    else{
                        this.selectedPhoneIndex = index;
                    }

                    $('#phoneModal').modal({
                        background: 'static'
                    });
                }
                else if('email' === type){
                    if(index === undefined){
                        this.selectedEmailIndex = this.member.emails.length;
                        this.member.emails.push($.extend({}, emptyEmail));
                    }
                    else{
                        this.selectedEmailIndex = index;
                    }

                    $('#emailModal').modal({
                        background: 'static'
                    });
                }
            },
            preferredChange(type){
                let oldVal;
                let selectedIndex;
                let array;
                if('address' === type){
                    oldVal = this.member.addresses[this.selectedAddressIndex].preferred;
                    array = this.member.addresses;
                    selectedIndex = this.selectedAddressIndex;
                }
                else if('phone' === type){
                    oldVal = this.member.phones[this.selectedPhoneIndex].preferred;
                    array = this.member.phones;
                    selectedIndex = this.selectedPhoneIndex;
                }
                else if('email' === type){
                    oldVal = this.member.emails[this.selectedEmailIndex].preferred;
                    array = this.member.emails;
                    selectedIndex = this.selectedEmailIndex;
                }

                if(oldVal === false){
                    //Because we get the old value here, we're looking to see if the value is false because it's about to become true
                    $.each(array, (index, element) => {
                        if(selectedIndex === index){
                            return true;
                        }

                        element.preferred = false;
                    });
                }
            }
        }
    }
</script>