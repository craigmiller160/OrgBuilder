<template>
    <div class="row">
        <div class="text-center col-xs-12 col-sm-6 col-sm-offset-3">
            <h2>Welcome to {{ orgName }}</h2>
        </div>
    </div>
</template>

<script>
    export default {
        name: 'main',
        props: [
            'orgName'
        ]
    }
</script>