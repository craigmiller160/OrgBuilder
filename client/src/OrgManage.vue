<template>
    <div>
        <div class="row">
            <div class="col-xs-12 text-center">
                <h2>Orgs</h2>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <!-- TODO table goes here -->
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <a class="btn btn-primary" href="/#/orgs/content.html" title="Add new Org">Add</a>
            </div>
        </div>
    </div>
</template>

<script>
    import { orgbuilder } from './js/orgbuilder.js';

    export default {
        name: 'org_manage',
        data(){
            return {

            }
        },
        beforeMount(){
            orgbuilder.access.hasMasterAccess(this);
        }
    }
</script>