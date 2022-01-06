<template>
  <div>
    <router-link to="/">...Floors</router-link>
    <div>User {{ $route.params.id }}</div>
  </div>
</template>

<script>
export default {
  name: "Room",
  components: {},
};
</script>
