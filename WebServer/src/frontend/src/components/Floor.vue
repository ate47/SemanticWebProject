<template>
  <li class="">
    <router-link :to="'/floor/' + encodeURIComponent(floor.iri)">
      {{ floor.label }}
    </router-link>
  </li>
</template>

<script>
import api from "../api";
export default {
  name: "floor",
  props: ["floor"],
  data: function () {
    return {
      rooms: [],
    };
  },
  methods: {
    loadRoom: function () {
      api
        .rooms(this.floor.iri)
        .then((rooms) => {
          this.rooms = rooms;
        })
        .catch(console.error);
    },
  },
};
</script>

<style>
</style>