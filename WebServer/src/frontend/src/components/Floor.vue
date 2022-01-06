<template>
  <div>{{ floor.label }} : {{ floor.iri }}</div>
</template>

<script>
export default {
  name: "floor",
  props: ["floor"],
};
</script>

<style>
.cursor-pointer {
  cursor: pointer;
}
</style>