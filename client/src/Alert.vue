<template>
    <div>
        <div v-show="alert.show" class="row">
            <div class="col-xs-12 col-sm-8 col-sm-offset-2">
                <div id="actionAlert" class="alert fade">
                    <a href="#" class="close" data-hide="alert" aria-label="close">&times;</a>
                    <p>{{ alert.msg }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'alert',
        props: [
            'alert'
        ],
        data() {
            return {

            }
        },
        watch: {
            alert(){
                if(this.alert.show){
                    $("#actionAlert").addClass(this.alert.clazz).addClass("in");
                }
            }
        }
    }
</script>