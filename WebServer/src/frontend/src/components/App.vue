<template>
  <div>
    <debug />
    <floors />
  </div>
</template>

<script>
import Debug from "./Debug.vue";
import Floors from "./Floors.vue";

export default {
  components: { Debug, Floors },
  name: "app",
};
</script>

<style>
</style>
