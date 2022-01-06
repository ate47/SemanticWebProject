<template>
  <li class="bg-white border-2 border-gray-200">
    <span>
      <div @click="loadRoom" class="cursor-pointer inline">
        {{ floor.label }} :
        <span class="text-gray-500">({{ floor.iri }})</span>
      </div>
      <ul v-if="rooms.length !== 0">
        <li :key="room" v-for="room in rooms" class="pl-2">
          {{ room.label }}
          <span class="text-gray-500">({{ room.iri }})</span>
        </li>
      </ul>
    </span>
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